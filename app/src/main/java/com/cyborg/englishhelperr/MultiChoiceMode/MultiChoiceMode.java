package com.cyborg.englishhelperr.MultiChoiceMode;

import android.content.Context;
import android.support.annotation.CallSuper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;

import com.cyborg.englishhelperr.MainActivity;

import java.util.Arrays;


public abstract class MultiChoiceMode implements ActionMode.Callback {

    private int menuResourceId;
    private ActionMode mActionMode;
    private boolean hasDeletionOccurred;
    private OnControlItemStateListener onControlItemStateListener;
    private final StateTracker mStateTracker=new StateTracker();

    private AppCompatActivity mAppCompatActivity;

    public MultiChoiceMode(int menuResourceId, Context context) {
        mAppCompatActivity = (AppCompatActivity) (context);
        this.menuResourceId=menuResourceId;
    }


    public interface OnControlItemStateListener {
        void onUpdateAll();
        void onUpdateAfterDeletion(int[] mCheckedIndices);
        void onCheckAll();
        void onUnCheckAll();

    }

    @Override
    //@CallSuper
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        this.mActionMode = actionMode;


        MenuInflater menuInflater = actionMode.getMenuInflater();
        menuInflater.inflate(menuResourceId, menu);
        //mAppCompatActivity.getMenuInflater().inflate(menuResourceId, menu);
        //mAppCompatActivity.getSupportActionBar().hide();              //скрываем actionbar
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    //@CallSuper
    public void onDestroyActionMode(ActionMode actionMode) {

        mActionMode=null;
        if(!hasDeletionOccurred) {
            onControlItemStateListener.onUpdateAll();
            mStateTracker.clear();
        }
        hasDeletionOccurred=false;
        mAppCompatActivity.getSupportActionBar().show();              //показываем actionbar
    }

    public void checkAll() {
        onControlItemStateListener.onCheckAll();
    }


    public void unCheckAll() {
        onControlItemStateListener.onUnCheckAll();
    }

    private static class StateTracker {

        private SparseBooleanArray mStateItemTracker=new SparseBooleanArray();
        private int mCheckedItemCount=0;

        public void selectItem(int position) {
            mStateItemTracker.put(position, !isSelected(position));
            mCheckedItemCount+=mStateItemTracker.get(position,false)?1:-1;
        }

        public boolean isSelected(int position) {
            return mStateItemTracker.get(position);
        }


        public int[] getSelectedItemArray() {

            int[] selectedItemArray = new int[mStateItemTracker.size()];
            int jIndex = 0;
            for (int index = 0; index < mStateItemTracker.size(); index++) {
                if (mStateItemTracker.get(mStateItemTracker.keyAt(index))) {
                    mStateItemTracker.put(mStateItemTracker.keyAt(index), false);
                    selectedItemArray[jIndex++] = mStateItemTracker.keyAt(index);
                }
            }

            Arrays.sort(selectedItemArray,0,jIndex);
            int itemShift=0;
            int[] resultArray=new int[jIndex];

            for(int index=0;index<jIndex;index++,itemShift++)
                resultArray[index]=selectedItemArray[index]-itemShift;

            mCheckedItemCount=0;
            return resultArray;
        }

        public boolean isNothingSelected() {
            return mCheckedItemCount == 0;
        }


        public void clear() {
            for (int index = 0; index < mStateItemTracker.size(); index++)
                mStateItemTracker.put(index, false);
            mCheckedItemCount=0;
        }

        public int getCheckedItemCount() {
            return mCheckedItemCount;
        }

    }


    public boolean isSelected(int position) {
        return mStateTracker.isSelected(position);
    }

    public void selectItem(int position) {
        mStateTracker.selectItem(position);
        if(mStateTracker.isNothingSelected())
            mActionMode.finish();
        else if(mActionMode!=null)
            mActionMode.setTitle("Selected:"+Integer.toString(mStateTracker.getCheckedItemCount()));

    }

    public void setUpdateListener(/*@NonNull*/ OnControlItemStateListener onControlItemStateListener) {
        this.onControlItemStateListener=onControlItemStateListener;
    }

    public void deleteSelected() {
        hasDeletionOccurred = true;
        mActionMode.finish();
        if(!mStateTracker.isNothingSelected())
            onControlItemStateListener.onUpdateAfterDeletion(mStateTracker.getSelectedItemArray());
    }


    public boolean isActivated() {
        return mActionMode != null;
    }


}