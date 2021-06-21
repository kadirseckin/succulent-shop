package school.cactus.succulentshop.product.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentProductListBinding
import school.cactus.succulentshop.infra.BaseFragment

class ProductListFragment : BaseFragment() {
    private var _binding: FragmentProductListBinding? = null

    private val binding get() = _binding!!

    override val viewModel: ProductListViewModel by viewModels {
        ProductListViewModelFactory(
            store = JwtStore(requireContext()),
            repository = ProductListRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.productlist_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.menuItemClicked(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}