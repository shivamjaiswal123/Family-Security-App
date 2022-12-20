package com.example.myfamily

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    //Storing all the contacts
    private val contacts: ArrayList<ContactModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //List of members that will be displayed on home fragment
        val listMember = listOf<MemberModel>(
            MemberModel(
                "Shivam",
                "Sulsan building, Tairan 9th road, Futian Shenzan Sulsan building, Tairan 9th road, Futian Shenzan",
                "89%",
                "466"
            ),

            MemberModel(
                "Ryan",
                "Sulsan Street, Tairan 5th road, Futian Shenzan Sulsan building, Tairan 5th road, Futian Shenzan",
                "45%",
                "222"
            ),
            MemberModel(
                "Shanvi",
                "7332 Paris Hill Drive, Harleysville, PA 19438",
                "10%",
                "789"
            ),
            MemberModel(
                "Ross",
                "1713 Coffman Alley,  Bowling Green, Kentucky, 42101, United States",
                "45%",
                "222"
            ),
            MemberModel(
                "Cal",
                "760 South Greenview Ave,Campbell, CA 95008",
                "23%",
                "156"
            )
        )


        //Setting the recycler for members
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MemberAdapter(listMember)

        //List of contact whome user can send invite
//        val listcontacts = listOf<ContactModel>(
//            ContactModel("Shivam", 639497444),
//            ContactModel("Cal", 89745697451),
//            ContactModel("Joey", 3247896474),
//            ContactModel("Rach", 12478964),
//            ContactModel("Pheebs", 34124789),
//
//        )

        val inviteAdapter = InviteAdapter(contacts)
        //using coroutines for fetching contacts in background thread coz its long process and will block the UI
        CoroutineScope(Dispatchers.IO).launch {

            contacts.addAll(fetchContacts())
            //inserting all contacts in (room)database
            insertContactsInDB(contacts)

            withContext(Dispatchers.Main) {
                inviteAdapter.notifyDataSetChanged()
            }
        }

        //setting the recycler for contacts
        recycler_invite.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler_invite.adapter = inviteAdapter


    }


    //made this as suspend function becoz insertAll is suspend function that can be called from coroutines or suspend func
    private suspend fun insertContactsInDB(contacts: ArrayList<ContactModel>) {
        val database = MyFamilyDatabase.getDatabase(requireContext())
        database.contactDao().insertAll(contacts)
    }

    //Fetching the contact from phone using content provider
    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<ContactModel> {
        val contentResolver = requireActivity().contentResolver
        val cursor =
            contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"
            )

        //storing all the contacts in listContatcs
        val listContacts: ArrayList<ContactModel> = ArrayList()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val columnId: String = ContactsContract.Contacts._ID
                val cursorIndex: Int = cursor.getColumnIndex(columnId)
                val id = cursor.getString(cursorIndex)

                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {

                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        ""
                    )

                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            val phoneNum =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            //listContacts.add(ContactModel(name, phoneNum))
                            if (!listContacts.contains(ContactModel(name, phoneNum))) {
                                listContacts.add(ContactModel(name, phoneNum))
                            }
                        }
                        phoneCursor.close()
                    }
                }

            }
            if (cursor != null) {
                cursor.close()
            }
        }
        return listContacts
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}