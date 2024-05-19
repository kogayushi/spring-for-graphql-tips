package kogayushi.springforgraphqltips.controller.post

import kogayushi.springforgraphqltips.controller.like.dto.Like
import kogayushi.springforgraphqltips.controller.post.dto.CreatePostInput
import kogayushi.springforgraphqltips.controller.post.dto.Post
import kogayushi.springforgraphqltips.controller.thread.dto.Thread
import kogayushi.springforgraphqltips.controller.user.dto.User
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap

@Controller
class PostController {

    private val inMemory = ConcurrentHashMap<Long, Post>().apply {
        put(1L, Post(id = 1L, content = "Content of post 1", authorId = 1L, threadId = 1L))
        put(2L, Post(id = 2L, content = "Content of post 2", authorId = 2L, threadId = 1L))
    }

    @MutationMapping
    fun createPost(@Argument input: CreatePostInput): Post {

        val post = Post(
            id = inMemory.size.toLong() + 1,
            content = input.content,
            authorId = input.authorId,
            threadId = input.threadId
        )
        inMemory[post.id] = post
        return post
    }

    @QueryMapping
    fun posts(): List<Post> {
        return inMemory.values.toList()
    }

    @BatchMapping(typeName = "Thread", field = "posts")
    fun postsOfThread(threads: List<Thread>): Map<Thread, List<Post>> {
        return threads.associateWith { thread ->
            inMemory.values.filter { it.threadId == thread.id }
        }
    }

    @BatchMapping(typeName = "User", field = "posts")
    fun postsOfUser(users: List<User>): Map<User, List<Post>> {
        return users.associateWith { user ->
            inMemory.values.filter { it.authorId == user.id }
        }
    }

    @BatchMapping(typeName = "Like", field = "post")
    fun postOfLike(likes: List<Like>): Map<Like, Post> {
        return likes.associateWith { like ->
            inMemory[like.postId] ?: throw IllegalStateException("Post not found")
        }
    }
}
