package zpi.auth;

import java.util.List;

import zpi.MainWindow;
import zpi.hadoopModel.User;
import zpi.tabs.ZpiTab;
import zpi.windows.DeleteUserWindow;
import zpi.windows.EditUserWindow;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Zakladka do zarzadzania uzytkownikami.
 *
 */
@SuppressWarnings("serial")
public class ManageUsersTab extends ZpiTab {

	public ManageUsersTab(MainWindow window) {
		super(window);
	}

	@Override
	public void createGUI() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		removeAllComponents();
		Label tableLabel = new Label("Zarz¹dzanie u¿ytkownikami");
		tableLabel.setStyleName("h2");
		
		List<User> otherUsers = User.getUsers();
		Table table = new Table();
		if (otherUsers != null) {
			table.setStyleName("strong borderless");
			table.setPageLength(otherUsers.size());
			table.addContainerProperty("Nazwa u¿ytkownika", String.class, null);
			table.addContainerProperty("Has³o", String.class, null);
			table.addContainerProperty("Edytuj u¿ytkownika", Button.class, null);
			table.addContainerProperty("Usuñ u¿ytkownika", Button.class, null);

			for (User user : otherUsers) {
				String name = user.getName();
				String password = user.getPassword();
				Button editButton = new Button("Edytuj");
				editButton.addListener(new EditUserListener(user));
				Button deleteButton = new Button("Usuñ");
				deleteButton.addListener(new DeleteUserListener(user));
				table.addItem(new Object[] { name, password, editButton,
						deleteButton }, user);
			}
		}
		Button newUserButton = new Button("Dodaj nowego u¿ytkownika");
		newUserButton.addListener(new AddUserListener(table));
		
		layout.addComponent(tableLabel);
		layout.addComponent(table);
		layout.addComponent(newUserButton);
		addComponent(layout);
	}

	private class DeleteUserListener implements Button.ClickListener {

		private User user = null;

		public DeleteUserListener(User user) {
			this.user = user;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			Window deleteUserWindow = new DeleteUserWindow(user, ManageUsersTab.this);
			deleteUserWindow.setModal(true);
			getWindow().addWindow(deleteUserWindow);

		}

	}
	
	private class EditUserListener implements Button.ClickListener {

		private User user = null;

		public EditUserListener(User user) {
			this.user = user;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			Window editUserWindow = new EditUserWindow(user, false, "Edytowanie u¿ytkownika", ManageUsersTab.this);
			editUserWindow.setModal(true);
			getWindow().addWindow(editUserWindow);

		}

	}
	
	private class AddUserListener implements Button.ClickListener {

		Table table;
		
		public AddUserListener(Table table){
			this.table = table;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			
			User newUser = new User();
			newUser.setName("");
			newUser.setPassword("");
			Button editButton = new Button("Edytuj");
			editButton.addListener(new EditUserListener(newUser));
			Button deleteButton = new Button("Usuñ");
			deleteButton.addListener(new DeleteUserListener(newUser));
			Window editUserWindow = new EditUserWindow(newUser, true, "Dodawanie u¿ytkownika", ManageUsersTab.this);
			editUserWindow.setModal(true);
			getWindow().addWindow(editUserWindow);

		}

	}

}
