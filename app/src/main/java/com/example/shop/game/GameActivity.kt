package com.example.shop

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shop.getMoney.GetMoneyBody
import com.example.shop.getMoney.GetMoneyData
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.timeout.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameActivity : AppCompatActivity() {

    var handler = Handler()
    var handler3 = Handler()
    var i:Long = 10
    var aiScore:Int = 0
    var userScore:Int =0
    private lateinit var apiInterface: APIInterface
    lateinit var shared :SharedPreferences
    lateinit var token:String
    lateinit var body:GetMoneyBody
    lateinit var userName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        shared = SharedPreferences(this)
        token = shared.preference.getString("token", "")?:""
        userName = shared.preference.getString("loginName", "")?:""
        score2.setText("${userName}分數：0")

        val pref_myself = sharedPreference_myself(this@GameActivity )
        val my_duration=pref_myself.get_duration_my_self()?.toLong()
        val my_img=pref_myself.get_img_my_self()?.toInt()
        val pref_competitor = sharedPreference_competitor(this@GameActivity )
        val competitor_delay_millis=pref_competitor.get_delay_millis_competitor()?.toLong()
        val competitor_duration=pref_competitor.get_duration_competitor()?.toLong()
        val competitor_img=pref_competitor.get_img_competitor()?.toInt()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.234.60.173")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)

        overridePendingTransition(0, 0)
        valueAnimator(competitor_duration!!)
        valueAnimator2(my_duration!!)
        my_image(my_imv,my_img!!)
        my_image(competitor_imv,competitor_img!!)

        my_imv.setOnClickListener {
            animator2.start()
            userScore+=1
            score2.setText("${userName}分數：$userScore")
        }

        val runnable3 = object :Runnable{
            override fun run() {
                handler3.postDelayed(this ,1000)
                i -= 1
                text.setText("倒數計時:00:$i")
                if(i<10){
                    text.setText("倒數計時:00:0$i")
                }
                if (i<=0){
                    handler3.removeCallbacks(this)
                    text.setText("倒數計時:00:00")
                }
            }
        }
        handler3.postDelayed(runnable3,1000)

        val runnable = object :Runnable{
            override fun run() {
                handler.postDelayed(this ,competitor_delay_millis!!)
                aiScore += 1
                score.setText("Arkadia分數：$aiScore")
                animator.start()
                if (i<=0){
                    handler.removeCallbacks(this)
                    my_imv.isEnabled = false
                    Thread.sleep(1350)
                    val mDialogView = LayoutInflater.from(this@GameActivity).inflate(R.layout.timeout, null)
                    val mBuilder = AlertDialog.Builder(this@GameActivity)
                        .setView(mDialogView)
                        .show()
                    mDialogView.my_score.setText("${userName}分數：$userScore")
                    my_image(mDialogView.my_imageView,my_img)
                    my_image(mDialogView.competitor_imageView,competitor_img)
                    mDialogView.competitor_totalscore.setText("Arkadia分數：$aiScore")
                    if (aiScore>userScore){
                        mDialogView.competitror_crown.visibility =View.VISIBLE
                    }
                    else if(aiScore<userScore){
                        mDialogView.my_crown.visibility =View.VISIBLE
                        body = GetMoneyBody(token)
                        apiInterface.getMoney(body).enqueue(object :retrofit2.Callback<GetMoneyData>{
                            override fun onFailure(call: Call<GetMoneyData>, t: Throwable) {
                                Toast.makeText(this@GameActivity, "Failure", Toast.LENGTH_LONG).show()
                            }

                            override fun onResponse(call: Call<GetMoneyData>, response: Response<GetMoneyData>) {
                                if (response.isSuccessful){
                                    val data = response.body()
                                    shared.setBalance(data!!.after_sheep.balance.toString())
                                    Toast.makeText(this@GameActivity, data.msg, Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(this@GameActivity, "QQ", Toast.LENGTH_LONG).show()
                                }
                            }
                        })
                    }
                    else{
                        mDialogView.competitror_crown.visibility =View.VISIBLE
                        mDialogView.my_crown.visibility =View.VISIBLE
                    }
                    mDialogView.return_page.setOnClickListener {
                                    pref_myself.delete_myself()
                                    pref_competitor.delete_competitor()
                                    this@GameActivity.finish()
                    }
                }
            }
        }
        handler.postDelayed(runnable,competitor_delay_millis!!)
    }


    private lateinit var animator: ValueAnimator

    fun valueAnimator(duration:Long) {
        animator= ValueAnimator.ofFloat(0.0f, -500.0f, 0.0f)
        animator.duration = duration
        animator.addUpdateListener {
            Log.wtf("aaaa","valueAnimator()")
            competitor_imv.translationY = it.animatedValue as Float

        }
    }

    private lateinit var animator2:ValueAnimator

    fun valueAnimator2(duration:Long) {
        animator2= ValueAnimator.ofFloat(0.0f, -500.0f, 0.0f)
        animator2.duration = duration
        animator2.addUpdateListener {
            Log.wtf("aaaa","valueAnimator()")
            my_imv.translationY = it.animatedValue as Float
        }
    }


    fun my_image(image:ImageView,number:Int){

        if(number == 1){
            image.setImageResource(R.drawable.dragon)
        }
        else if(number == 2){
            image.setImageResource(R.drawable.turtle)
        }
        else if(number == 3){
            image.setImageResource(R.drawable.frog)
        }
        else {
            image.setImageResource(R.drawable.chun)
        }
    }
}
