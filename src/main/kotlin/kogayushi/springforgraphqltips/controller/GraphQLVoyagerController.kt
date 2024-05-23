package kogayushi.springforgraphqltips.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GraphQLVoyagerController {

    @GetMapping("/graphql-voyager")
    fun index(): String {
        return "graphql-voyager.html"
    }
}
