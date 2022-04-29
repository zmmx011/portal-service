package com.invenia.portalservice.api.notify;

import com.invenia.portalservice.rabbitmq.DestinationsConfig;
import com.invenia.portalservice.rabbitmq.DestinationsConfig.DestinationInfo;
import com.invenia.portalservice.rabbitmq.MessageListenerContainerFactory;
import java.time.Duration;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@SuppressWarnings({"SpringElInspection", "ELValidationInspection"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
@CrossOrigin("http://localhost:3000")
public class NotifyController {

  private final AmqpAdmin amqpAdmin;

  private final DestinationsConfig destinationsConfig;

  private final MessageListenerContainerFactory messageListenerContainerFactory;

  @PostConstruct
  public void setupTopicDestinations() {
    destinationsConfig.getTopics().forEach((key, destination) -> {
          log.info("Creating TopicExchange: name={}, exchange={}", key, destination.getExchange());
          amqpAdmin.declareExchange(
              ExchangeBuilder.topicExchange(destination.getExchange())
              .durable(true)
              .build());
          log.info("Topic Exchange successfully created.");
        });
  }

  @GetMapping(value = "/subscribe/{id}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
  @PreAuthorize("principal.getClaimAsString('preferred_username') == #id")
  public Flux<?> subscribe(@PathVariable String id) {

    DestinationInfo destinationInfo = destinationsConfig.getTopics().get("notify");
    if (destinationInfo == null) {
      return Flux.just(ResponseEntity.notFound().build());
    }
    Queue topicQueue = createTopicQueue(destinationInfo, id);
    String qname = topicQueue.getName();
    MessageListenerContainer mlc = messageListenerContainerFactory.createMessageListenerContainer(qname);
    Flux<String> flux = Flux.create(emitter -> {
      mlc.setupMessageListener(message -> emitter.next(new String(message.getBody())));
      emitter.onRequest(value -> mlc.start());
      emitter.onDispose(() -> {
        amqpAdmin.deleteQueue(qname);
        mlc.stop();
      });
    });

    return Flux.interval(Duration.ofSeconds(5))
        .map(v -> "")
        .mergeWith(flux);
  }

  private Queue createTopicQueue(DestinationsConfig.DestinationInfo destination, String routingKey) {

    Exchange exchange = ExchangeBuilder.topicExchange(destination.getExchange())
        .durable(true)
        .build();

    amqpAdmin.declareExchange(exchange);

    Queue queue = QueueBuilder.nonDurable().build();

    amqpAdmin.declareQueue(queue);

    Binding binding = BindingBuilder.bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();

    amqpAdmin.declareBinding(binding);

    return queue;
  }

}
