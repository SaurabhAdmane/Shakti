package com.shakticoin.app.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shakticoin.app.R
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.bounty.BountyRepository
import com.shakticoin.app.api.bounty.OfferModel
import com.shakticoin.app.databinding.ActivityBountyBinding
import com.shakticoin.app.widget.CheckableRoundButton

class BonusBountyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBountyBinding
    lateinit var viewModel: BonusBountyModel
    private lateinit var bountyRepository: BountyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bounty)
        (binding as ViewDataBinding?)!!.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(BonusBountyModel::class.java)
        binding.viewModel = viewModel

        binding.selector.setHasFixedSize(true)
        binding.selector.layoutManager = LinearLayoutManager(this)

        bountyRepository = BountyRepository()
        bountyRepository.setLifecycleOwner(this)

        binding.progressBar.visibility = View.VISIBLE
        bountyRepository.getOffers(object : OnCompleteListener<List<OfferModel>>() {
            override fun onComplete(value: List<OfferModel>?, error: Throwable?) {
                binding.progressBar.visibility = View.INVISIBLE
                if (error != null) {
                    return
                }
                val offers = value?.sortedBy { it.expiryDate }?.filter { !it.expired }
                binding.selector.adapter = OfferAdapter(offers)
                viewModel.selectedBonus.value = offers?.first()
                binding.doClaim.isEnabled = true
            }
        })
    }

    fun onClaim(v: View?) {
        val intent = Intent(this, ReferralActivity::class.java)
        startActivity(intent)
//        val activity = this
//        val offer = viewModel.selectedBonus.value
//        if (offer != null) {
//            binding.progressBar.visibility = View.VISIBLE
//            bountyRepository.claimBonusBounty(offer.id, null, object : OnCompleteListener<BountyReferralViewModel>() {
//                override fun onComplete(value: BountyReferralViewModel?, error: Throwable?) {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    if (error != null) {
//                        MessageBox(Debug.getFailureMsg(activity, error)).show(supportFragmentManager, null)
//                        return
//                    }
//                    val intent = Intent(activity, NewBonusBountyActivity::class.java)
//                    startActivity(intent)
//                }
//            })
//        }
    }

    fun onViewPromotion(v: View?) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show()
    }

    inner class SelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var button : CheckableRoundButton? = null

        init {
            button = itemView.findViewById(R.id.selectorButton)
            button?.setOnCheckedChangeListener { buttonView: CheckableRoundButton, isChecked: Boolean ->
                if (isChecked) {
                    val selectedOffer = buttonView.tag as OfferModel
                    viewModel.selectedBonus.value = selectedOffer

                    // when an button is checked we should make unchecked the rest
                    val items: Int = binding.selector.adapter!!.itemCount
                    for (i in 0 until items) {
                        val vh : SelectorViewHolder? = binding.selector.findViewHolderForAdapterPosition(i) as SelectorViewHolder?
                        if (vh != null) {
                            val offer = vh.button?.tag as OfferModel
                            vh.setChecked(selectedOffer.id == offer.id)
                        }
                    }
                }
            }
        }

        fun setData(model: OfferModel) {
            button!!.tag = model
        }

        fun setChecked(isChecked: Boolean) {
            button!!.isChecked = isChecked
        }
    }

    inner class OfferAdapter(private val items: List<OfferModel>?) : RecyclerView.Adapter<SelectorViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
            return SelectorViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_selector, parent, false))
        }

        override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
            holder.setData(items!![position])
            val selectedOffer = viewModel.selectedBonus.value
            if (selectedOffer != null) {
                holder.setChecked(selectedOffer.id == items[position].id)
            }
        }

        override fun getItemCount(): Int {
            if (items != null) {
                return items.size
            }
            return 0
        }

    }
}