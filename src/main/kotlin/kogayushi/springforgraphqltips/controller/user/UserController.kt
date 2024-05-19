package kogayushi.springforgraphqltips.controller.user

import kogayushi.springforgraphqltips.controller.like.dto.Like
import kogayushi.springforgraphqltips.controller.thread.dto.Thread
import kogayushi.springforgraphqltips.controller.user.dto.User
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap

@Controller
class UserController {

    private val inMemory = ConcurrentHashMap<Long, User>().apply {
        put(1L, User(id = 1L, username = "user1", email = "user1@example.com"))
        put(2L, User(id = 2L, username = "user2", email = "user2@example.com"))
    }

    @QueryMapping
    fun users(): List<User> {
        // ダミーの値を返す
        return inMemory.values.toList()
    }

    @BatchMapping(typeName = "Thread", field = "author")
    fun authorOfUser(threads: List<Thread>): Map<Thread, User> {
        return threads.associateWith { thread ->
            inMemory[thread.authorId] ?: throw IllegalStateException("User not found")
        }
    }

    @BatchMapping(typeName = "Like", field = "user")
    fun userOfLike(likes: List<Like>): Map<Like, User> {
        return likes.associateWith { like ->
            inMemory[like.userId] ?: throw IllegalStateException("User not found")
        }
    }
}
