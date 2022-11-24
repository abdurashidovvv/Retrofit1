package com.abdurashidov.retrofit1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.abdurashidov.retrofit1.adapters.RvAdapter
import com.abdurashidov.retrofit1.databinding.ActivityMainBinding
import com.abdurashidov.retrofit1.databinding.DialogItemBinding
import com.abdurashidov.retrofit1.models.AddTodoRequest
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        postData()
        return super.onOptionsItemSelected(item)
    }

    private fun getData() {
        ApiClient.getRetrofitService().getAllTodo().enqueue(object : Callback<ArrayList<Plan>> {
            override fun onResponse(
                call: Call<ArrayList<Plan>>, response: Response<ArrayList<Plan>>
            ) {
                rvAdapter = RvAdapter(response.body()!!, this@MainActivity)
                binding.myRv.adapter = rvAdapter
            }

            override fun onFailure(call: Call<ArrayList<Plan>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity, "Internet bilan muammo bor.", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun postData() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogItemBinding = DialogItemBinding.inflate(layoutInflater)
        dialog.setView(dialogItemBinding.root)
        dialog.show()

        dialogItemBinding.apply {
            save.setOnClickListener {
                Toast.makeText(this@MainActivity, "Save button click!", Toast.LENGTH_SHORT).show()
                val addTodoRequest = AddTodoRequest(
                    holat = status.selectedItem.toString(),
                    matn = text.text.toString(),
                    oxirgi_muddat = deadline.text.toString(),
                    sarlavha = title.text.toString()
                )
                ApiClient.getRetrofitService().addTodo(addTodoRequest)
                    .enqueue(object : Callback<Plan> {
                        override fun onResponse(call: Call<Plan>, response: Response<Plan>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "${response.body()?.sarlavha} saqlandi!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getData()
                            }
                        }

                        override fun onFailure(call: Call<Plan>, t: Throwable) {
                            Toast.makeText(
                                this@MainActivity,
                                "Internet bilan bogliq muammo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                dialog.cancel()
            }
        }

    }

    override fun onClick(label: Plan) {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
    }
}