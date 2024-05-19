package kogayushi.springforgraphqltips.controller.post.dto

data class CreatePostInput(
    val content: String,
    val authorId: Long,
    val threadId: Long
)
