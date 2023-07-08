package app.regate.map.fragment.establecimiento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.regate.map.R

class EstablecimientoReservaFragment : Fragment() {

//    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.content_main, container, false)
    }
//        return ComposeView(requireContext()).apply {
//            setContent {
//                Column(modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())){
//                    repeat(200){
//
//                Text(text = "Hello world")
//                        Spacer(modifier = Modifier.height(20.dp))
//                    }
//                }
//            }
//        }
//        return inflater.inflate(
//                R.layout.fragment_mock_list, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val items = resources.getStringArray(R.array.mock_items_games)
//        val adapter = MockListAdapter(items)
//        recyclerView = view.findViewById(R.id.recycler_view)
//        val layoutManager = LinearLayoutManager(context)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter
//    }

