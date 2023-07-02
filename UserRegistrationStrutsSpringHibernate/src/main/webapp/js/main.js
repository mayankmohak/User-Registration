Ext.define('User', {
	extend: 'Ext.data.Model',
	fields: ['username', 'email']
});

let store = Ext.create('Ext.data.Store', {
	model: 'User'
});

let headerPanel = Ext.create('Ext.form.Panel', {
	width: 400,
	height: 80,
	layout: {
		type: 'hbox', align: 'center', pack: 'space-between'
	},
	bodyPadding: 10,
	margin: '0 0 10 0',
	items: [
		{
			xtype: 'label',
			text: 'Hyd Readiness 2023',
			width: 240,
			style: {
				fontSize: '16px',
				fontWeight: 'bold'
			}
		},
		{
			xtype: 'image',
			src: './img/logo.png',
			width: 130,
			height: 25,
			style: {
				objectFit: 'fit'
			}
		}
	]

});


let formPanel = Ext.create('Ext.form.Panel', {
	width: 400,
	height: 250,
	layout: {
		type: 'vbox', align: 'center', pack: 'center'
	},
	bodyPadding: 10,
	allowBlank: false,
	required: true,
	buttonAlign: 'center',
	buttonCls: 'register-btn',
	defaults: {
		anchor: '100%'
	},
	defaultType: 'textfield',

	items: [
		{
			id: 'username',
			name: 'username',
			fieldLabel: 'Username',
			allowBlank: false,
			emptyText: 'Username'
		},
		{
			name: 'email',
			fieldLabel: 'Email',
			allowBlank: false,
			emptyText: 'Email Address',
			vtype: "email",
			errorTip: {
				anchor: true,
				align: 'l-r?'
			},
			errorTarget: 'under'
		},
		{
			fieldLabel: 'Password',
			name: 'password',
			inputType: 'password',
			allowBlank: false,
			emptyText: 'Password',
			validators: function(value) {
				return /^[a-zA-Z]{4,}$/.test(value) ? true : 'Your password must contain at least 8 characters';
			},
		}, {
			id: 'confirm_password',
			fieldLabel: 'Confirm Password',
			name: 'confirm_password',
			inputType: 'password',
			allowBlank: false,
			emptyText: 'Confirm Password',
		},
		{
			xtype: 'button',
			text: 'Register',
			scale: 'medium',
			margin: '5, 0,0 ,0',
			width: 150,
			formBind: true,
			handler: function() {
				let form = this.up('form').getForm();
				let password = form.findField('password').getValue();
				let confirmpassword = form.findField('confirm_password').getValue();
				let username = form.findField('username').getValue();
				let email = form.getValues().email;

				if (form.isValid() && password === confirmpassword) {
					const onSuccess = (res) => {						
						let jsonRes = Ext.util.JSON.decode(res.responseText);
						jsonRes = Ext.util.JSON.decode(jsonRes.response);
						if (jsonRes.success === false && jsonRes.msg === "User Already present") {
							let name = form.findField('username');
							name.focus();
							Ext.Msg.alert({
								title: 'Error',
								msg: `User Already Registered.<br/> UserName : ${username}<br/> Email :${email}`,
								buttons: Ext.Msg.OKCANCEL
							})
						} else if (jsonRes.success === true) {
							Ext.create('Ext.window.Window', {
								title: 'Success',
								modal: true,
								border: false,
								buttonAlign: 'center',
								items: [{
									xtype: 'panel',
									border: false,
									width: 250,
									height: 55,
									padding: 10,
									layout: 'center',
									html: 'Your are successfully registerd as ' + username
								}],
								buttons: [{
									text: 'Close',
									handler: function() {
										this.up('window').close();
									}
								}]

							}).show();
							form.reset();
						} else {
							let name = form.findField('username');
							name.focus();
							Ext.Msg.alert({
								title: 'Error',
								msg: `Some Error with Server onSuccess.`,
								buttons: Ext.Msg.OKCANCEL
							})
						}
					};
					const onFailure = () => {
						Ext.Msg.alert({
							title: 'Error',
							msg: `Some Error with Server in onFailure.`,
							buttons: Ext.Msg.OKCANCEL
						})
					};
					Ext.Ajax.request({
						url: 'http://localhost:8080/UserRegistrationStrutsSpringHibernate/register',
						success: onSuccess,
						failure: onFailure,
						params: { password: password, email: email, username: username },
						method: "POST"
					});
				} else if (password !== confirmpassword) {

					Ext.Msg.alert('Password Mismatch', 'Please enter the same password!')
					let confirmPasswordField = form.findField('confirm_password');
					confirmPasswordField.focus();

				} else {
					Ext.Msg.alert('Error', 'Please Fill out all the forms!');
				}
			}

		}
	],
	listeners: []

});

let parentPanel = Ext.create('Ext.panel.Panel', {
	width: 420,
	height: 400,
	bodyPadding: '5, 10,10,10',
	items: [headerPanel, formPanel]
});


Ext.onReady(function() {

	let size = Ext.getBody().getViewSize();
	let marginWidth = size.width;
	let mainContainer = Ext.create('Ext.container.Container', {
		width: 'fullScreen',
		height: '100%',
		items: [parentPanel]
	})

	Ext.create('Ext.container.Container', {
		width: 'fullScreen',
		height: '100%',
		layout: 'center',
		bodyPadding: 10,
		margin: `${marginWidth / 7}, 0, 0,0`,
		renderTo: Ext.getBody(),
		items: [mainContainer]
	})

});