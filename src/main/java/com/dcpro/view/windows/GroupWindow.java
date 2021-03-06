package com.dcpro.view.windows;

import com.dcpro.entities.Group;
import com.dcpro.dao.GroupDAO;
import com.dcpro.dao.GroupDAOImpl;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

public abstract class GroupWindow extends Window {

    private final FormLayout form = new FormLayout();
    private final HorizontalLayout buttonsLayout = new HorizontalLayout();
    private final TextField groupNumberField = new TextField("Номер группы");
    private final TextField facultyField = new TextField("Факультет");
    private final Button okButton = new Button("OK", FontAwesome.CHECK);
    private final Button cancelButton = new Button("Отмена", FontAwesome.CLOSE);
    private final GroupDAO dao = new GroupDAOImpl();
    private int groupNumber;
    private String faculty;
    private Group group;

    public GroupWindow() {
        super();
        formInit();
        setModal(true);
        setContent(form);
    }

    public Button getOkButton() {
        return okButton;
    }

    public FormLayout getForm() {
        return form;
    }

    public HorizontalLayout getButtonsLayout() {
        return buttonsLayout;
    }

    public TextField getGroupNumberField() {
        return groupNumberField;
    }

    public TextField getFacultyField() {
        return facultyField;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public GroupDAO getDao() {
        return dao;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public String getFaculty() {
        return faculty;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
        groupNumberField.setValue(String.valueOf(group.getGroupNumber()));
        facultyField.setValue(group.getFaculty());
    }

    private void formInit() {
        buttonsLayoutInit();
        form.addComponents(groupNumberField, facultyField, buttonsLayout);
        groupNumberField.setRequired(true);
        facultyField.setRequired(true);
        form.setMargin(true);
        form.setSpacing(true);
    }

    private void buttonsLayoutInit() {
        buttonsLayout.addComponents(okButton, cancelButton);
        okButtonAddClickListener();
        cancelButton.addClickListener((Button.ClickEvent e) -> {
            this.close();
        });
        buttonsLayout.setSpacing(true);
    }

    protected abstract void okButtonAddClickListener();

    protected boolean isValidFieldData() {
        try {
            groupNumber = Integer.parseInt(groupNumberField.getValue().trim());
            faculty = facultyField.getValue().trim();
            if (dao.getByNumberAndFaculty(groupNumber, faculty) != null) {
                Notification.show("Группа уже существует!", Notification.Type.ERROR_MESSAGE);
                return false;
            }
            if (group == null) {
                group = new Group();
            }
            group.setGroupNumber(groupNumber);
            group.setFaculty(faculty);
            if(faculty.length() == 0) {
                Notification.show("Введите название факультета!", Notification.Type.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            Notification.show("Введите номер группы! Он должен быть целым положительным числом",
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
