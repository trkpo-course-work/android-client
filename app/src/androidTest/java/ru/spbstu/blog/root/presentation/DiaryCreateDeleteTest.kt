package ru.spbstu.blog.root.presentation


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
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
class DiaryCreateDeleteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun diaryCreateDeleteTest() {
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
        bottomNavigationItemView.perform(click())

        val tabView = onView(
            allOf(
                withContentDescription("Дневник"),
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

        val floatingActionButton = onView(
            allOf(
                withId(R.id.frg_user_diary__fab),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.frg_post__et_post),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("Test diary"), closeSoftKeyboard())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.frg_post__finish_post),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_post__toolbar),
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
        appCompatImageButton.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val viewGroup = onView(
            withId(R.id.layout_notes__rv_posts),
        )
        viewGroup.perform(object: ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(
                    isAssignableFrom(RecyclerView::class.java),
                    isDisplayed()
                )
            }
            override fun getDescription(): String {
                return "Scroll to 0"
            }
            override fun perform(uiController: UiController?, view: View?) {
                view as RecyclerView
                view.scrollToPosition(0)
                uiController?.loopMainThreadUntilIdle()
            }
        })
        viewGroup.check(ViewAssertions.matches(atPosition(0, isDisplayed())))
        onView(isRoot()).perform(waitFor(1000))

        onView(withId(R.id.layout_notes__rv_posts))
            .check(ViewAssertions.matches(atPosition(0, hasDescendant(withText("Test diary")))))

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.item_note__iv_actions),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_notes__rv_posts),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val materialTextView = onView(
            allOf(
                withId(android.R.id.title), withText("Редактировать"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatImageButton3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.frg_post__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.item_note__iv_actions),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_notes__rv_posts),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val materialTextView2 = onView(
            allOf(
                withId(android.R.id.title), withText("Удалить"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        onView(withId(R.id.layout_notes__rv_posts))
            .check(
                ViewAssertions.matches(
                    atPosition(
                        0,
                        hasDescendant(Matchers.not(withText("Test diary")))
                    )
                )
            )
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
