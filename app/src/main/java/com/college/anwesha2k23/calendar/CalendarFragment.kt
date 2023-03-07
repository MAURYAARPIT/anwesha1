package com.college.anwesha2k23.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.anwesha2k23.R
import com.college.anwesha2k23.calendar.Adapters.EventAdapter
import com.college.anwesha2k23.calendar.Adapters.TimeAdapter
import com.college.anwesha2k23.calendar.Adapters.VerticalAdapter
import com.college.anwesha2k23.calendar.DataFiles.EventData
import com.college.anwesha2k23.calendar.Functions.ButtonsFunction
import com.college.anwesha2k23.calendar.Functions.CalendarFunctions
import com.college.anwesha2k23.databinding.FragmentCalendarBinding
import com.college.anwesha2k23.events.SingleEventFragment
import com.college.anwesha2k23.home.EventList
import com.college.anwesha2k23.home.EventsViewModel
import kotlin.math.log

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding

    private lateinit var recyclerViewEvents: RecyclerView
    private lateinit var recyclerViewTimeSlots: RecyclerView

    private lateinit var eventViewModel: EventsViewModel
    private lateinit var newEventList: ArrayList<EventList>
    private var usefull_list: List<EventData> = emptyList()
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.deliveryShimmer.startShimmer()

        recyclerViewTimeSlots = binding.recyclerViewTimeSlots
        recyclerViewTimeSlots.layoutManager = LinearLayoutManager(activity)
        recyclerViewTimeSlots.adapter = TimeAdapter()

        recyclerViewEvents = binding.recyclerViewLocations
        recyclerViewEvents.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        eventViewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        newEventList = arrayListOf()
        recyclerViewEvents.isNestedScrollingEnabled = false

        getEvents("16")

        binding.day1.setOnClickListener {
            ButtonsFunction().selectButton(
                binding.day1,
                requireActivity(),
                binding.day2,
                binding.day3
            )
            getEvent_bydate("16", newEventList)
        }
        binding.day2.setOnClickListener {
            ButtonsFunction().selectButton(
                binding.day2,
                requireActivity(),
                binding.day1,
                binding.day3
            )
            getEvent_bydate("17", newEventList)
        }
        binding.day3.setOnClickListener {
            ButtonsFunction().selectButton(
                binding.day3,
                requireActivity(),
                binding.day2,
                binding.day1
            )
            getEvent_bydate("18", newEventList)
        }
        return view
    }

    private fun getEvents(date: String) {
        eventViewModel.getEventListObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                newEventList = it
                newEventList.add(
                    EventList(
                        "abcde",
                        "TEST 1 ",
                        emptyList(),
                        "IIT PATNA",
                        "",
                        "2023-03-16T18:25:00Z",
                        "2023-03-16T23:25:00Z"
                    )
                )
                newEventList.add(
                    EventList(
                        "abcdf",
                        "TEST 9 ",
                        emptyList(),
                        "IIT PATNA",
                        "",
                        "2023-03-16T02:25:00Z",
                        "2023-03-16T06:25:00Z"
                    )
                )
                newEventList.add(
                    EventList(
                        "abcdg",
                        "TEST 2",
                        emptyList(),
                        "FOOD COURT",
                        "",
                        "2023-03-16T18:25:00Z",
                        "2023-03-16T19:25:00Z"
                    )
                )
                newEventList.add(
                    EventList(
                        "abcdh",
                        "TEST 3",
                        emptyList(),
                        "IIT PATNA",
                        "",
                        "2023-03-17T18:25:00Z",
                        "2023-03-17T19:25:00Z"
                    )
                )
                newEventList.add(
                    EventList(
                        "abcdi",
                        "TEST 4",
                        emptyList(),
                        "FOOD COURT",
                        "",
                        "2023-03-17T18:25:00Z",
                        "2023-03-17T19:25:00Z"
                    )
                )
                newEventList.add(
                    EventList(
                        "abcdj",
                        "TEST 3",
                        emptyList(),
                        "IIT PATNA",
                        "",
                        "2023-03-18T18:25:00Z",
                        "2023-03-18T19:25:00Z"
                    )
                )
                binding.visibleFrag.visibility = View.VISIBLE
                binding.deliveryShimmer.stopShimmer()
                binding.deliveryShimmer.visibility = View.GONE
                adapter = EventAdapter(newEventList, object : EventAdapter.OnItemClickListener {
                    override fun onItemClick(verticalItem: EventList) {
                        Log.d("12345", verticalItem.toString())
                        loadSingleEventFragment(verticalItem)
                    }
                })

                usefull_list = CalendarFunctions().Usefull_data(newEventList)

//                sortedEvents = events.sortedBy { it.startTime.split(":").first().toInt() }

                Log.d("abcd", it.toString())
//                val margins = CalendarFunctions().cal_margin(sortedEvents, requireActivity())
//
//                val decoration = CustomItemDecoration(margins)
//                recyclerViewEvents.addItemDecoration(decoration)
                recyclerViewEvents.adapter = adapter

                getEvent_bydate("16", newEventList)

//                val filteredList = usefull_list.filter { it.startdate == date.toString()}
//                adapter = EventAdapter(filteredList, newEventList)
////                adapter.setOnItemClickListener(object :
////                    EventAdapter.OnItemClickListener {
////                    override fun onItemClicked(event: EventList?) {          //when any event from the recycler view is clicked
////                        loadSingleEventFragment(event)
////                    }
////                })
//                recyclerViewEvents.adapter = adapter
            } else {
                Toast.makeText(context, "Error in getting Events", Toast.LENGTH_SHORT).show()
            }
        }

        eventViewModel.makeApiCall(requireContext())
    }

    fun getEvent_bydate(date: String, reallist: ArrayList<EventList>) {
        binding.deliveryShimmer.startShimmer()
        binding.deliveryShimmer.visibility = View.VISIBLE
        val filteredList = usefull_list.filter { it.startdate == date.toString() }.toMutableList()
        val event_by_Location = CalendarFunctions().get_events_by_location(filteredList)
        var eventList: List<List<EventData>> = mapToList(event_by_Location)
        var locationlist: List<String> = mapToKeys(event_by_Location)
        adapter.setList(filteredList, eventList, locationlist)
        adapter.notifyDataSetChanged()
        binding.visibleFrag.visibility = View.VISIBLE
        binding.deliveryShimmer.visibility = View.GONE
        binding.deliveryShimmer.stopShimmer()
    }

    fun mapToList(map: Map<String, List<EventData>>): List<List<EventData>> {
        val result = mutableListOf<List<EventData>>()
        for ((_, value) in map) {
            result.add(value)
        }
        return result
    }

    fun mapToKeys(map: Map<String, List<EventData>>): List<String> {
        val result = mutableListOf<String>()
        for ((key, value) in map) {
            result.add(key)
        }
        return result
    }

    private fun loadSingleEventFragment(event: EventList?) {
        val bundle = Bundle()
        bundle.putSerializable("event", event)
        val fragment = SingleEventFragment()
        fragment.arguments = bundle
        val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}