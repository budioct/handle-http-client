package budhioct.dev.service;

import budhioct.dev.dto.TodoDTO;
import budhioct.dev.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    //public Mono<TodoDTO> getTodoById(int id) {
    //    return webClient.get()
    //            .uri("/todos/{id}", id)
    //            .retrieve()
    //            .bodyToMono(TodoDTO.class);
    //}

    public Mono<TodoDTO> getTodoById(int id) {
        return webClient.get()
                .uri("/todos/{id}", id)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> {
                            if (response.statusCode() == HttpStatus.NOT_FOUND) {
                                return Mono.error(new ResourceNotFoundException("Todo with id " + id + " not found"));
                            }
                            return Mono.error(new RuntimeException("Client error"));
                        }
                )
                .bodyToMono(TodoDTO.class);
    }
}
