/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameWonBinding


class GameWonFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_won, container, false)

        // setOnClickListener on nextMatchButton and assign navigation action
        binding.nextMatchButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_gameWonFragment_to_gameFragment)}

        // Catch arguments from gameFragment
        val args = GameWonFragmentArgs.fromBundle(requireArguments())
        // Test correctness by toasting arguments
        Toast.makeText(context, "numCorrect: ${args.numCorrect} | numQuestions: ${args.numQuestions}", Toast.LENGTH_SHORT).show()

        // Create options menu
        setHasOptionsMenu(true)

        return binding.root
    }

    // Build intent and define content to share
    private fun getShareIntent() : Intent {

        // Get arguments from GameFragment
        val args = GameWonFragmentArgs.fromBundle(requireArguments())

        // Build shareIntent and define Action
        val shareIntent = Intent(Intent.ACTION_SEND)

        // Define content of the shareIntent
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_success_text,
                        args.numCorrect, args.numQuestions))

        // Return fresh built intent
        return shareIntent
    }

    // Open chooser and pass intent to its activity
    private fun shareSuccess(){
        startActivity(getShareIntent())
    }

    // Inflate options menu and show item dynamically
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // select menu to inflate
        inflater.inflate(R.menu.winner_menu, menu)

        // check Android package manager for suitable apps to share intent.
        // Options menu will be invisible if no suitable app is installed
        if(getShareIntent().resolveActivity(requireActivity().packageManager) == null) {

            // select item not to display in options menu
            menu.findItem(R.id.share).isVisible = false
        }
    }

    // Trigger shareIntent() when share item is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // (Learn following Syntax!!!) Pick function by Id!
        when (item.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
