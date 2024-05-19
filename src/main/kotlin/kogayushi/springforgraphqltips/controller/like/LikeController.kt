package kogayushi.springforgraphqltips.controller.like

import kogayushi.springforgraphqltips.controller.like.dto.Like
import kogayushi.springforgraphqltips.controller.like.dto.LikeInput
import kogayushi.springforgraphqltips.controller.post.dto.Post
import kogayushi.springforgraphqltips.controller.user.dto.User
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap

@Controller
class LikeController {

    private val inMemory = ConcurrentHashMap<Long, Like>().apply {
        put(1L, Like(id = 1L, userId = 1L, postId = 1L))
        put(2L, Like(id = 2L, userId = 2L, postId = 2L))
    }

    @MutationMapping
    fun like(@Argument input: LikeInput): Like {
        val like = Like(
            id = inMemory.size.toLong() + 1,
            userId = input.userId,
            postId = input.postId
        )
        inMemory[like.id] = like
        return like
    }

    @QueryMapping
    fun likes(): List<Like> {
        return inMemory.values.toList()
    }

    @BatchMapping(typeName = "Post", field = "likes")
    fun likesOfPost(posts: List<Post>): Map<Post, List<Like>> {
        return posts.associateWith { post ->
            inMemory.values.filter { it.postId == post.id }
        }
    }

    @BatchMapping(typeName = "User", field = "likes")
    fun likesOfUser(users: List<User>): Map<User, List<Like>> {
        return users.associateWith { user ->
            inMemory.values.filter { it.userId == user.id }
        }
    }
}
