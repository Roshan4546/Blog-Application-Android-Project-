import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlogItemModel(
    val heading2: String? = null,
    val username2: String? = null,
    val date2: String? = null,
    val post2: String? = null,
    val likeCounts2: Int? = 0
) : Parcelable
