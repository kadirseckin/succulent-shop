package school.cactus.succulentshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}