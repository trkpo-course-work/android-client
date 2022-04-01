package ru.spbstu.blog.root.presentation


import android.view.View
import android.view.ViewGroup
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainPagesTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java, true)

    @Test
    fun mainPagesTest() {
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

        val textView = onView(
            allOf(
                withId(R.id.frg_blog__toolbar_title), withText("ГЛАВНАЯ"),
                withParent(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("ГЛАВНАЯ")))

        val imageButton = onView(
            allOf(
                withId(R.id.frg_blog__refresh),
                withParent(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val imageButton2 = onView(
            allOf(
                withId(R.id.frg_blog__refresh),
                withParent(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))

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

        val textView2 = onView(
            allOf(
                withId(R.id.frg_profile__toolbar_title), withText("ПРОФИЛЬ"),
                withParent(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("ПРОФИЛЬ")))

        val imageButton3 = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                withParent(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton3.check(matches(isDisplayed()))

        val imageButton4 = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                withParent(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton4.check(matches(isDisplayed()))

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

        val textView3 = onView(
            allOf(
                withId(R.id.frg_blog__toolbar_title), withText("ГЛАВНАЯ"),
                withParent(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("ГЛАВНАЯ")))

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.diaryFragment), withContentDescription("Notifications"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView5 = onView(
            allOf(
                withText("БЛОГ"),
                withParent(
                    allOf(
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("БЛОГ")))

        val textView6 = onView(
            allOf(
                withText("ДНЕВНИК"),
                withParent(
                    allOf(
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("ДНЕВНИК")))

        val imageButton5 = onView(
            allOf(
                withId(R.id.frg_user_blog__fab),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        imageButton5.check(matches(isDisplayed()))

        val imageButton6 = onView(
            allOf(
                withId(R.id.frg_user_blog__fab),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        imageButton6.check(matches(isDisplayed()))

        val tabView = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frg_diary__tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val bottomNavigationItemView4 = onView(
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
        bottomNavigationItemView4.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView7 = onView(
            allOf(
                withId(R.id.frg_search__toolbar_title), withText("ПОИСК"),
                withParent(
                    allOf(
                        withId(R.id.frg_search__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("ПОИСК")))

        val editText = onView(
            allOf(
                withId(R.id.frg_search__et_search),
                withParent(
                    allOf(
                        withId(R.id.frg_search__mc_search),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText.check(matches(isDisplayed()))

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
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard())
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
