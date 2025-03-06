package budhioct.dev.controller;

import budhioct.dev.dto.TodoDTO;
import budhioct.dev.service.TodoFeignClient;
import budhioct.dev.service.TodoRestTemplateService;
import budhioct.dev.service.TodoWebClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/todos")
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
        return restTemplateService.getTodos();
    }

    @GetMapping("/webclient")
    public Flux<TodoDTO> getTodosWithWebClient() {
        return webClientService.getTodos();
    }

    @GetMapping("/feign")
    public List<TodoDTO> getTodosWithFeign() {
        return feignClient.getTodos();
    }
}
