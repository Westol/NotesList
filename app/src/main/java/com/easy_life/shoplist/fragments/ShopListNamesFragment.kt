package com.easy_life.shoplist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.easy_life.shoplist.activities.MainApp
import com.easy_life.shoplist.activities.ShopListActivity
import com.easy_life.shoplist.databinding.FragmentShopListNamesBinding
import com.easy_life.shoplist.db.MainViewModel
import com.easy_life.shoplist.db.ShopListNameAdapter
import com.easy_life.shoplist.dialogs.DeleteDialog
import com.easy_life.shoplist.dialogs.NewListDialog
import com.easy_life.shoplist.entities.ShopListNameItem
import com.easy_life.shoplist.utils.TimeManager


class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {

    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewMovelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCLickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String) {
                val shopListName = ShopListNameItem(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter
    }

    private fun observer(){
        mainViewModel.mAllShopListNamesItem.observe(viewLifecycleOwner, {
               adapter.submitList(it)
        })
    }



    companion object {
        @JvmStatic
        fun newInstance() =  ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShopList(id, true)
            }

        })
    }

    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String) {
                mainViewModel.updateListName(shopListNameItem.copy(name = name))
            }
        }, shopListNameItem.name)
    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {
         val i = Intent(activity, ShopListActivity::class.java).apply {
             putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
         }
        startActivity(i)
    }
}