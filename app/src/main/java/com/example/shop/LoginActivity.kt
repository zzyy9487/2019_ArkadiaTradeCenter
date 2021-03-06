package com.example.shop

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shop.login.LoginBody
import com.example.shop.login.LoginData
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_res
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    lateinit var shared :SharedPreferences
    lateinit var body: LoginBody
    var resstatus = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        shared = SharedPreferences(this)

        if(shared.preference.getString("resstatus", "").isNullOrEmpty()){
            resstatus = "0"
        }
        else{
            resstatus = "1"
        }

        if (resstatus == "1"){
            editLogaccount.setText(shared.preference.getString("resaccount", ""))
            editLogpass.setText(shared.preference.getString("respassword", ""))
        }

        btn_login.setOnClickListener {
            if(editLogaccount.text.isNullOrEmpty() or editLogpass.text.isNullOrEmpty()){
                Toast.makeText(this, "帳號密碼都要輸入！", Toast.LENGTH_LONG).show()
            }
            else{
                body = LoginBody(editLogaccount.text.toString(), editLogpass.text.toString())
                btn_login.isEnabled = false
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://35.229.181.103")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val apiInterface = retrofit.create(APIInterface::class.java)
                apiInterface.login(body).enqueue(object :retrofit2.Callback<LoginData>{
                    override fun onFailure(call: Call<LoginData>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "一定漏了什麼沒填...", Toast.LENGTH_LONG).show()
                        btn_login.isEnabled = true
                    }

                    override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                        if (response.code() == 200){
                            val data = response.body()
                            shared.setResStatus("1")
                            shared.setAccount(editLogaccount.text.toString())
                            shared.setResAccount(editLogaccount.text.toString())
                            shared.setResPssword(editLogpass.text.toString())
                            shared.setToken(data!!.now_sheep.api_token)
                            shared.setLoginName(data.now_sheep.name)
                            shared.setBalance(data.now_sheep.balance.toString())
                            shared.setLevel(data.lv.toString())
                            shared.setScore(data.now_sheep.score.toString())
                            shared.setId(data.now_sheep.id.toString())
                            shared.setScore2(data.last_lv.toString())
                            shared.setType(data.now_sheep.type)
                            Toast.makeText(this@LoginActivity, data.msg, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, BuyActivity::class.java)
                            startActivity(intent)
                            btn_login.isEnabled = true
                        }
                        else{
                            Toast.makeText(this@LoginActivity, "QQ...登入失敗...", Toast.LENGTH_LONG).show()
                            btn_login.isEnabled = true
                        }
                    }
                })

            }
        }

        btn_res.setOnClickListener {
            this.finish()
        }

        val font = Typeface.createFromAsset(assets, "fonts/Chalkduster.ttf")
        textViewCamp.setTypeface(font)
        textViewShop.setTypeface(font)
        textLogaccount.setTypeface(font)
        textLogpassword.setTypeface(font)
        btn_login.setTypeface(font)
        btn_res.setTypeface(font)

    }



}
