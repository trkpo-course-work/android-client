package ru.spbstu.blog.root.presentation


import android.text.InputFilter
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbstu.blog.R
import ru.spbstu.blog.root.presentation.utils.waitFor
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProfileEditTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java, true)

    @Test
    fun profileEditTest() {
        onView(isRoot()).perform(waitFor(1000))
        val appCompatEditText = onView(
            allOf(
                withId(R.id.layout_login__et_login),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_login__mc_login),
                        childAtPosition(
                            withId(R.id.layout_login__rl_root),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("viruskuls"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.layout_login__et_password),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_login__mc_password),
                        childAtPosition(
                            withId(R.id.layout_login__rl_root),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("imo39067"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("ВОЙТИ"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        materialButton.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(1000))

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.profileFragment), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val materialTextView = onView(
            allOf(
                withId(android.R.id.title), withText("Редактировать"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.android.internal.view.menu.ListMenuItemView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView = onView(
            allOf(
                withId(R.id.frg_edit_profile__toolbar_title), withText("РЕДАКТИРОВАНИЕ"),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("РЕДАКТИРОВАНИЕ")))

        val imageButton = onView(
            allOf(
                withId(R.id.frg_edit_profile__ib_actions),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val editText = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_name),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_name),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText.check(matches(isDisplayed()))
        editText.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 50 })
        }

        val editText2 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_login),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_login),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText2.check(matches(isDisplayed()))
        editText2.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val editText3 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_old_pass),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_old_pass),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText3.check(matches(isDisplayed()))

        val editText4 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_new_pass),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_new_pass),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText4.check(matches(isDisplayed()))
        editText4.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val editText5 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_conf_new_pass),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_conf_new_pass),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText5.check(matches(isDisplayed()))
        editText5.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_name),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_name),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        val randomName = getRandomString(20)
        appCompatEditText3.perform(replaceText(randomName), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_login),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_login),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            6
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("viruskuls"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_old_pass),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_old_pass),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            8
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("imo39067"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_new_pass),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_new_pass),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            10
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("imo39067"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.frg_edit_profile__et_conf_new_pass),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__mc_conf_new_pass),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            12
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(replaceText("imo39067"), closeSoftKeyboard())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.frg_edit_profile__ib_actions),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
        onView(isRoot()).perform(waitFor(2000))

        val textView3 = onView(
            allOf(
                withId(R.id.frg_profile__tv_name), withText(randomName),
                withParent(withParent(withId(R.id.bottomNavHost))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText(randomName)))
        mActivityTestRule.finishActivity()
    }

    companion object {
        private val ALLOWED_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm "
    }

    private fun getRandomString(sizeOfRandomString: Int): String {
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
