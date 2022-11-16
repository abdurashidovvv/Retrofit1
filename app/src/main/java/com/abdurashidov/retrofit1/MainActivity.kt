package com.abdurashidov.retrofit1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abdurashidov.retrofit1.adapters.RvAdapter
import com.abdurashidov.retrofit1.databinding.ActivityMainBinding
import com.abdurashidov.retrofit1.models.Plan
import com.abdurashidov.retrofit1.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), RvAdapter.RvClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.getRetrofitService().getAllTodo()
            .enqueue(object : Callback<ArrayList<Plan>>{
                override fun onResponse(
                    call: Call<ArrayList<Plan>>,
                    response: Response<ArrayList<Plan>>
                ) {
                    rvAdapter=RvAdapter(response.body()!!, this@MainActivity)
                    binding.myRv.adapter=rvAdapter
                }

                override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Internet bilan muammo bor.", Toast.LENGTH_SHORT).show()
                }
            })

    }

    override fun onClick(label: Plan) {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
    }
}