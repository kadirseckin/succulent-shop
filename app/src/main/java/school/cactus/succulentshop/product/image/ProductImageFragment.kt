package school.cactus.succulentshop.product.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import school.cactus.succulentshop.databinding.FragmentProductImageBinding

class ProductImageFragment : Fragment() {

    private var _binding: FragmentProductImageBinding? = null

    private val binding get() = _binding!!

    private val args: ProductImageFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductImageBinding.inflate(inflater, container, false)
        binding.imageUrl = args.productImageUrl
        return binding.root
    }
}