package kogayushi.springforgraphqltips.controller.tag

import kogayushi.springforgraphqltips.controller.tag.dto.Tag
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap

@Controller
class TagController {

    private val inMemory = ConcurrentHashMap<Long, Tag>().apply {
        put(1L, Tag(id = 1L, name = "Tag1"))
        put(2L, Tag(id = 2L, name = "Tag2"))
    }

    @QueryMapping
    fun tags(): List<Tag> {
        // ダミーの値を返す
        return inMemory.values.toList()
    }
}
