package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.view.InterrView;
import com.example.sportcentersristeiuioana.view.MyGroupsView;

public class MyGroupsController extends ApplicationStartController{
    MyGroupsView view;
    GroupRepository repository;

    public void setView(MyGroupsView view) {
        this.view = view;
    }

    public void setRepository(GroupRepository repository) {
        this.repository = repository;
    }

    public void exitGroup(String groupName, int memberId){
        InterrView view1 = new InterrView("Are You Sure?");
        view1.getButton().setText("Yes");
        view1.getButton().setOnAction(e -> {
            view1.getStage().close();
            int groupId = repository.getGroupId(groupName);
            boolean exit = repository.deleteMemberAndCheckGroup(groupId, memberId);
            if(exit) {
                InterrView view2 = new InterrView("You are not part of the group anymore!");
            }
            else{
                InterrView view2 = new InterrView("Error!");
            }
        });
    }
}
