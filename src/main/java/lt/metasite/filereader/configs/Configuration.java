package lt.metasite.filereader.configs;

import lt.metasite.filereader.factories.AppExecutorService;
import lt.metasite.filereader.factories.CharacterHolderFactory;
import lt.metasite.filereader.factories.FileRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.concurrent.ExecutorService;

@org.springframework.context.annotation.Configuration
@EnableWebSocketMessageBroker
public class Configuration implements WebSocketMessageBrokerConfigurer {


    @Bean
    @Qualifier("front")
    public ExecutorService executorServiceFront(){
        return new AppExecutorService().getInstance();
    }

    @Bean
    @Qualifier("repo")
    public ExecutorService executorServiceRepo(){
        return new AppExecutorService(FileRepo.values().length).getInstance();
    }

    @Bean
    public CharacterHolderFactory factory(){
        return new CharacterHolderFactory();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat").withSockJS();
    }
}
