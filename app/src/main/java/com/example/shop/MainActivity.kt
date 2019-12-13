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
    var type:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shared = SharedPreferences(this)

        if (shared.preference.getString("resstatus", "") == "1"){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        radioFire.setOnClickListener{
            radioFire.isChecked = true
            radioGrass.isChecked = false
            radioWater.isChecked = false
        }

        radioWater.setOnClickListener{
            radioFire.isChecked = false
            radioWater.isChecked = true
            radioGrass.isChecked = false

        }

        radioGrass.setOnClickListener{
            radioFire.isChecked = false
            radioWater.isChecked = false
            radioGrass.isChecked = true
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
                                if (radioFire.isChecked){
                                    type = "fire"
                                } else if (radioWater.isChecked){
                                    type = "water"
                                } else type = "grass"
                                btn_res.isEnabled = false

                                body = ResBody(editResname.text.toString(), editResaccount.text.toString(), editRespass.text.toString(), type)
                                val retrofit = Retrofit.Builder()
                                    .baseUrl("http://35.234.60.173")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                val apiInterface = retrofit.create(APIInterface::class.java)
                                val call = apiInterface.res(body)

                                call.enqueue(object :retrofit2.Callback<ResData>{
                                    override fun onFailure(call: Call<ResData>, t: Throwable) {
                                        Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
                                        btn_res.isEnabled = true
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
                                            btn_res.isEnabled = true
                                        }
                                        else {
                                            Toast.makeText(this@MainActivity, "帳號重複哦！", Toast.LENGTH_LONG).show()
                                            btn_res.isEnabled = true
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
            btn_log.isEnabled = false
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            btn_log.isEnabled = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
