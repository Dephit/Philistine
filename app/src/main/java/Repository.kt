import com.sergeenko.alexey.noble.apis.NobleApi
import com.sergeenko.alexey.noble.dataclasses.Club

interface Repository {

    suspend fun auth(api: NobleApi, email: String, password: String): Club?
}