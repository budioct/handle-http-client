package budhioct.dev.service;

import budhioct.dev.dto.TodoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "jsonplaceholder", url = "https://jsonplaceholder.typicode.com")
public interface TodoFeignClient {

    @GetMapping("/todos/")
    List<TodoDTO> getTodos();

    @GetMapping("/todos/{id}")
    TodoDTO getTodoById(@PathVariable(name = "id") int id);

}
