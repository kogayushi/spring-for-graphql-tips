package kogayushi.springforgraphqltips.controller.thread.dto

import kogayushi.springforgraphqltips.controller.user.dto.User
import kogayushi.springforgraphqltips.controller.post.dto.Post
import kogayushi.springforgraphqltips.controller.tag.dto.Tag

data class Thread(
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val posts: List<Post> = listOf(),
    val tags: List<Tag> = listOf()
)
