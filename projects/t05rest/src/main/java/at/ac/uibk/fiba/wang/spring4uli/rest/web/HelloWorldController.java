package at.ac.uibk.fiba.wang.spring4uli.rest.web;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloWorldController {

    @RequestMapping("")
    public ResponseEntity<String> hello() {
        return hello("World");
    }


    @RequestMapping("/to/{name}")
    public ResponseEntity<String> hello(@PathVariable("name") String name) {
        return ResponseEntity.ok("Hello " + name.trim() + "!");
    }

    @RequestMapping("/with/{name}")
    public String helloAgain(@PathVariable("name") String name) {
        return "Hallo" + name.trim() + "!";
    }

}
