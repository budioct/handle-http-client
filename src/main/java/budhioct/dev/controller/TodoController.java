package budhioct.dev.controller;

import budhioct.dev.dto.TodoDTO;
import budhioct.dev.service.TodoFeignClient;
import budhioct.dev.service.TodoRestTemplateService;
import budhioct.dev.service.TodoWebClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/todos")
@Slf4j
public class TodoController {

    private final TodoRestTemplateService restTemplateService;
    private final TodoWebClientService webClientService;
    private final TodoFeignClient feignClient;

    public TodoController(TodoRestTemplateService restTemplateService, TodoWebClientService webClientService, TodoFeignClient feignClient) {
        this.restTemplateService = restTemplateService;
        this.webClientService = webClientService;
        this.feignClient = feignClient;
    }

    @GetMapping("/resttemplate")
    public List<TodoDTO> getTodosWithRestTemplate() {
        log.info("Request received for method getTodosWithRestTemplate()");
        return restTemplateService.getTodos();
    }

    @GetMapping("/webclient")
    public Flux<TodoDTO> getTodosWithWebClient() {
        log.info("Request received for method getTodosWithWebClient()");
        return webClientService.getTodos();
    }

    @GetMapping("/feign")
    public List<TodoDTO> getTodosWithFeign() {
        log.info("Request received for method getTodosWithFeign()");
        return feignClient.getTodos();
    }

    @GetMapping("/resttemplate/{id}")
    public TodoDTO getTodoWithRestTemplate(@PathVariable(name = "id") int id) {
        log.info("Request received for method getTodoWithRestTemplate() with id: {}", id);
        return restTemplateService.getTodoById(id);
    }

    @GetMapping("/webclient/{id}")
    public Mono<TodoDTO> getTodoWithWebClient(@PathVariable(name = "id") int id) {
        log.info("Request received for method getTodoWithWebClient() with id: {}", id);
        return webClientService.getTodoById(id);
    }

    @GetMapping("/feign/{id}")
    public TodoDTO getTodoWithFeign(@PathVariable(name = "id") int id) {
        log.info("Request received for method getTodoWithFeign() with id: {}", id);
        return feignClient.getTodoById(id);
    }
}
