package ru.spbstu.blog.root.presentation


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbstu.blog.R
import ru.spbstu.blog.root.presentation.utils.atPosition
import ru.spbstu.blog.root.presentation.utils.waitFor

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchAndAddToFavoriteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun searchAndAddToFavoriteTest() {
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
        appCompatEditText.perform(replaceText("viruskulsz"), closeSoftKeyboard())

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
                withId(R.id.searchFragment), withContentDescription("Notifications"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.frg_search__et_search),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_search__mc_search),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("svy"), closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(4000))

        val materialCheckBox = onView(
            allOf(
                withId(R.id.item_search__favorite),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frg_search__rv_search_results),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialCheckBox.perform(click())
        onView(isRoot()).perform(waitFor(4000))

        val materialCheckBox2 = onView(
            allOf(
                withId(R.id.item_search__favorite),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frg_search__rv_search_results),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialCheckBox2.perform(click())
        onView(isRoot()).perform(waitFor(4000))

        val recyclerView = onView(
            allOf(
                withId(R.id.frg_search__rv_search_results),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        onView(isRoot()).perform(waitFor(1000))

        val viewGroup2 = onView(
            withId(R.id.frg_user_profile__rv_posts),
        )
        viewGroup2.check(ViewAssertions.matches(atPosition(0, isDisplayed())))
        val viewGroup3 = onView(
            withId(R.id.frg_user_profile__rv_posts),
        )
        viewGroup3.check(ViewAssertions.matches(atPosition(1, isDisplayed())))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.frg__user_profile__iv_back),
                childAtPosition(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        childAtPosition(
                            withId(R.id.frg_user_profile__appBarLayout),
                            1
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.frg_search__rv_search_results),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        onView(isRoot()).perform(waitFor(4000))

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.frg__user_profile__ib_favorite),
                childAtPosition(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        childAtPosition(
                            withId(R.id.frg_user_profile__appBarLayout),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())
        onView(isRoot()).perform(waitFor(4000))

        val appCompatImageView3 = onView(
            allOf(
                withId(R.id.frg__user_profile__ib_favorite),
                childAtPosition(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        childAtPosition(
                            withId(R.id.frg_user_profile__appBarLayout),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView3.perform(click())
        onView(isRoot()).perform(waitFor(4000))

        val appCompatImageView4 = onView(
            allOf(
                withId(R.id.frg__user_profile__iv_back),
                childAtPosition(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        childAtPosition(
                            withId(R.id.frg_user_profile__appBarLayout),
                            1
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView4.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.blogFragment), withContentDescription("Dashboard"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())
        onView(isRoot()).perform(waitFor(1000))
        mActivityTestRule.finishActivity()
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
