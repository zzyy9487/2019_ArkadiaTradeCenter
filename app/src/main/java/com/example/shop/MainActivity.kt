package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shop.res.ResBody
import com.example.shop.res.ResData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var body: ResBody
    lateinit var shared :SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shared = SharedPreferences(this)

        if (shared.preference.getString("resstatus", "") == "1"){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        btn_res.setOnClickListener {
            if(editResname.text.isNullOrEmpty() or editRespass.text.isNullOrEmpty() or editResaccount.text.isNullOrEmpty()){
                Toast.makeText(this, "一定漏了什麼沒填...", Toast.LENGTH_LONG).show()
            }
            else{
                if (editResname.text.length > 20){
                    Toast.makeText(this, "暱稱字數最高20...", Toast.LENGTH_LONG).show()
                }
                else{
                    if (editResaccount.text.length > 20){
                        Toast.makeText(this, "帳戶字數最高20...", Toast.LENGTH_LONG).show()
                    }
                    else{
                        if (editRespass.text.length < 8 ){
                            Toast.makeText(this, "密碼字數最少8位...", Toast.LENGTH_LONG).show()
                        }
                        else{
                            if (editRespass.text.length > 12){
                                Toast.makeText(this, "密碼字數最高12位...", Toast.LENGTH_LONG).show()
                            }
                            else{
                                body = ResBody(editResname.text.toString(), editResaccount.text.toString(), editRespass.text.toString())
                                val retrofit = Retrofit.Builder()
                                    .baseUrl("http://35.234.60.173")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                val apiInterface = retrofit.create(APIInterface::class.java)
                                val call = apiInterface.res(body)

                                call.enqueue(object :retrofit2.Callback<ResData>{
                                    override fun onFailure(call: Call<ResData>, t: Throwable) {
                                        Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
                                    }

                                    override fun onResponse(call: Call<ResData>, response: Response<ResData>) {
                                        if (response.code() == 200){
                                            val data = response.body()
                                            shared.setResName(data!!.create_date.name)
                                            shared.setResAccount(data.create_date.account)
                                            shared.setResPssword(editRespass.text.toString())
                                            shared.setResStatus("1")
                                            Toast.makeText(this@MainActivity, data.msg, Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                        }
                                        else {
                                            Toast.makeText(this@MainActivity, "帳號重複哦！", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                })
                            }
                        }

                    }

                }





            }
        }

        btn_log.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
