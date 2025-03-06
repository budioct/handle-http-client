package budhioct.dev.service;

import budhioct.dev.dto.TodoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class TodoWebClientService {

    private final WebClient webClient;

    public TodoWebClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public Flux<TodoDTO> getTodos() {
        return webClient.get()
                .uri("/todos/")
                .retrieve()
                .bodyToFlux(TodoDTO.class);
    }
}
