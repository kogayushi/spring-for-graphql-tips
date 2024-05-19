package kogayushi.springforgraphqltips.controller.like.dto

data class Like(
    val id: Long,
    val userId: Long,
    val postId: Long
)
