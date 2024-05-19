package kogayushi.springforgraphqltips.controller.thread.dto

data class CreateThreadInput(
    val title: String,
    val content: String,
    val authorId: Long
)
