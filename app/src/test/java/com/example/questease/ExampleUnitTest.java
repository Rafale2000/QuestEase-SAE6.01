//package com.example.questease;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.robolectric.RobolectricTestRunner;
//
//import static org.junit.Assert.*;
//
//import android.content.SharedPreferences;
//import android.view.View;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(RobolectricTestRunner.class)
//public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//
//    }
//
//    @Test
//    public void testInitializePreference() {
//        SharedPreferences mockSharedPreferences = Mockito.mock(SharedPreferences.class);
//        SharedPreferences.Editor mockEditor = Mockito.mock(SharedPreferences.Editor.class);
//
//
//        Mockito.when(mockSharedPreferences.contains("test_key")).thenReturn(false);
//        Mockito.when(mockSharedPreferences.edit()).thenReturn(mockEditor);
//        Mockito.when(mockEditor.putInt("test_key", 42)).thenReturn(mockEditor);
//
//        Parametres parametres = new Parametres();
//        parametres.sharedPreferences = mockSharedPreferences;
//        parametres.initializePreference("test_key", 42);
//
//        Mockito.verify(mockEditor).putInt("test_key", 42);
//        Mockito.verify(mockEditor).apply();
//    }
//
//    @Test
//    public void testAdjustTextSize() {
//
//        TextView mockTextView1 = Mockito.mock(TextView.class);
//        TextView mockTextView2 = Mockito.mock(TextView.class);
//
//        List<View> views = new ArrayList<>(Arrays.asList(
//                mockTextView1, mockTextView2
//        ));
//        Parametres parametres = new Parametres();
//        parametres.adjustTextSize(views);
//
//        Mockito.verify(mockTextView1).setTextSize(20);
//        Mockito.verify(mockTextView2).setTextSize(20);
//    }
//
//}