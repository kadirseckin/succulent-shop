package school.cactus.succulentshop.api.product

data class ImageInfo(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val formats: ImageFormats,
)