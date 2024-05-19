package kogayushi.springforgraphqltips.controller.thread

import kogayushi.springforgraphqltips.controller.post.dto.Post
import kogayushi.springforgraphqltips.controller.tag.dto.Tag
import kogayushi.springforgraphqltips.controller.thread.dto.CreateThreadInput
import kogayushi.springforgraphqltips.controller.thread.dto.Thread
import kogayushi.springforgraphqltips.controller.user.dto.User
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap

@Controller
class ThreadController {

    private val inMemory = ConcurrentHashMap<Long, Thread>().apply {
        put(1L, Thread(id = 1L, title = "Thread 1", content = "Content of thread 1", authorId = 1L))
        put(2L, Thread(id = 2L, title = "Thread 2", content = "Content of thread 2", authorId = 1L))
    }

    @MutationMapping
    fun createThread(@Argument input: CreateThreadInput): Thread {
        val newThread = inMemory.keys.maxOrNull()?.let { maxId ->
            Thread(id = maxId + 1, title = input.title, content = input.content, authorId = input.authorId)
        } ?: Thread(id = 1, title = input.title, content = input.content, authorId = input.authorId)

        inMemory[newThread.id] = newThread

        return newThread
    }

    @QueryMapping
    fun threads(): List<Thread> {
        return inMemory.values.toList()
    }

    @BatchMapping(typeName = "Tag", field = "threads")
    fun threadsOfTag(tags: List<Tag>): Map<Tag, List<Thread>> {
        return tags.associateWith { tag ->
            inMemory.values.filter { it.tags.contains(tag) }
        }
    }

    @BatchMapping(typeName = "Post", field = "thread")
    fun threadOfPost(posts: List<Post>): Map<Post, Thread> {
        return posts.associateWith { post ->
            inMemory[post.threadId] ?: throw IllegalStateException("Thread not found")
        }
    }

    @BatchMapping(typeName = "User", field = "threads")
    fun threadsOfUser(users: List<User>): Map<User, List<Thread>> {
        return users.associateWith { user ->
            inMemory.values.filter { it.authorId == user.id }
        }
    }
}
