package org.example.capstonedesign1.domain.conversation.service;

import static org.example.capstonedesign1.global.constant.ThreadPoolConstant.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.bankproduct.service.BankProductQueryService;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.service.CardProductQueryService;
import org.example.capstonedesign1.domain.conversation.dto.json.ConversationResponseContent;
import org.example.capstonedesign1.domain.conversation.dto.request.ConversationRequest;
import org.example.capstonedesign1.domain.conversation.dto.response.ConversationResponse;
import org.example.capstonedesign1.domain.conversation.entity.Conversation;
import org.example.capstonedesign1.domain.conversation.repository.ConversationRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.dto.response.Message;
import org.example.capstonedesign1.global.exception.InternalServerException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.openai.client.OpenAiClient;
import org.example.capstonedesign1.global.openai.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.example.capstonedesign1.global.weaviate.WeaviateHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConversationCommandService {

	private final BankProductQueryService bankProductQueryService;
	private final CardProductQueryService cardProductQueryService;
	private final WeaviateHandler weaviateHandler;

	private final ConversationRepository conversationRepository;

	private final ThreadPoolTaskExecutor taskExecutor;
	private final OpenAiClient openAiClient;

	public ConversationResponse conversation(User user, boolean embedding, ConversationRequest request) {
		String requestMessage = request.requestMessage();
		Float[] queryVector = openAiClient.sendEmbeddingRequest(requestMessage);
		NearVectorArgument nearVector = getNearVector(queryVector);

		try {
			CompletableFuture<List<BankProduct>> bankProducts = getSimilarBankProducts(nearVector);
			CompletableFuture<List<CardProduct>> cardProducts = getSimilarCardProducts(nearVector);
			CompletableFuture.allOf(bankProducts, cardProducts).join();

			String prompt = PromptTemplate.conversationPrompt(user, requestMessage,
				Optional.ofNullable(request.summary()),
				bankProducts.get(), cardProducts.get());
			String response = openAiClient.sendRequest(List.of(new Message(OpenAiClient.SYSTEM_ROLE, prompt)));
			ConversationResponseContent content = JsonUtil.parseClass(ConversationResponseContent.class, response);

			Conversation conversation = new Conversation(user, requestMessage, content.message(), content.summary());
			Conversation savedConversation = conversationRepository.save(conversation);
			return ConversationResponse.from(savedConversation);
		} catch (ExecutionException e) {
			throw new InternalServerException(ErrorCode.ASYNC_ERROR, e.getMessage());
		} catch (InterruptedException e) {
			throw new InternalServerException(ErrorCode.THREAD_INTERRUPT, e.getMessage());
		}
	}

	public CompletableFuture<List<BankProduct>> getSimilarBankProducts(NearVectorArgument nearVector) {
		return CompletableFuture.supplyAsync(() -> {
			List<UUID> bankProductIds = weaviateHandler.getSimilarBankProduct(nearVector);
			return bankProductQueryService.findByIds(bankProductIds);
		}, taskExecutor);
	}

	@Async(I_O_TASK_THREAD_POOL_NAME)
	public CompletableFuture<List<CardProduct>> getSimilarCardProducts(NearVectorArgument nearVector) {
		List<UUID> cardProductIds = weaviateHandler.getSimilarCardProduct(nearVector);
		return CompletableFuture.completedFuture(cardProductQueryService.findByIds(cardProductIds));
	}

	private NearVectorArgument getNearVector(Float[] queryVector) {
		return NearVectorArgument.builder()
			.vector(queryVector)
			.certainty(0.7f) // 유사도 임계값
			.build();
	}

}
