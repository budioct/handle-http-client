package budhioct.dev.service;

import budhioct.dev.dto.TodoDTO;

import budhioct.dev.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TodoRestTemplateService {

    private final RestTemplate restTemplate;

    public TodoRestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TodoDTO> getTodos() {
        String url = "https://jsonplaceholder.typicode.com/todos/";
        TodoDTO[] todos = restTemplate.getForObject(url, TodoDTO[].class); // public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException
        return Arrays.asList(todos != null ? todos : new TodoDTO[0]);
    }

    //public TodoDTO getTodoById(int id) {
    //    String url = "https://jsonplaceholder.typicode.com/todos/" + id;
    //    TodoDTO data = restTemplate.getForObject(url, TodoDTO.class);
    //    return data;
    //}

    public TodoDTO getTodoById(int id) {
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;
        try {
            TodoDTO data = restTemplate.getForObject(url, TodoDTO.class);
            return data;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Todo with id " + id + " not found");
            }
            throw e;
        }
    }
}
