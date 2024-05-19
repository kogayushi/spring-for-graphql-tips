package kogayushi.springforgraphqltips.controller.post.dto

data class Post(
    val id: Long,
    val content: String,
    val authorId: Long,
    val threadId: Long,
    val likeIds: List<Long> = listOf()
)
