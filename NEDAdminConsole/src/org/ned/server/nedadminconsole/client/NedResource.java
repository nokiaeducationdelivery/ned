/*******************************************************************************
* Copyright (c) 2011-2012 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.server.nedadminconsole.client;

import com.google.gwt.i18n.client.Constants;

public interface NedResource extends Constants{
    
    String update();
    String close();
    String username();
    String ok();
    String cancel();
    String message();
    String select();
    String id();
    String name();
    String error();
    String save();
    String delete();
    String userManagement();
    String yes();
    String no();
    String add();
    
    String containerLibrary();
    
    String motdMessageOfTheDay();
    String motdAddNewMessage();
    String motdCurrentMessage();
    String motdEmptyTextArea();
       
    String userDlgAddNewUser();
    String userDlgAddUser();
    String userDlgAlreadyInUse();
    String userDlgPassword();
    String userDlgNewPassword(); 
    String userDlgSaveModifications();
    String userDlgPasswordEmpty(); 
    String userDlgPasswordIdentical();
    String userDlgRepeatPassword();
    String userDlgChangesNotSaved();
    
    String libDlgChooseLibrary();
    String libDlgSelectLibrary();
    String libDlgNewLibrary();
    String libDlgLoading();
    
    String newElemDlgAddNew();
    String newElemDlgRandomize();
    String newElemDlgCheck();
    String newElemDlgIdIsEmpty();
    String newElemDlgIdIllegal();
    
    String uploadDlgUploadFile();
    String uploadDlgUpload();
    String uploadDlgMsgEmpty();
    String uploadDlgMsgWav();
    String uploadErrorMultipartContent();
    String uploadErrorBadRequest();
    String uploadErrorWrongFileType();
    String uploadErrorDatabaseUpdate();
    String uploadSuccess();
    String uploadingFile();
    
    String itemEditElementInfo();
    String itemEditParentId();
    String itemEditTitle();
    String itemEditType();
    String itemEditDescription();
    String itemEditKeywords();
    String itemFile();
    String itemEditExternalLinks();
    String itemEditNoParent();
    String itemEditNoChanges();
    String itemNoFile();
    
    String mainOpenLibrary();
    String mainNew(); 
    String mainLibrary();
    String mainCatalog();
    String mainCategory();
    String mainMediaItem();
    String mainMOTD();
    String mainStatistics();
    String mainLanguage();
    
    String statisticsDownload();
    String downloadAllStats();
    String downloadSelectDate();
    String statisticsFrom();
    String statisticsTo();
    String statisticsChooseMethod();
    String statisticsWrongDate();
    
    String mainTabLibraryManager();
    
    String msgPartNew();
    String msgPartAdded();
    String msgErrorAddingItem();
    String msgIdIsFree();
    String msgIdNotAvailable();
    String msgItemNotDeleted();
    String msgNoLibraryFound();
    String msgItemUpdated();
    String msgItemNotUpdated();
    String msgCannotUpdateMotd();
    String msgMotdUpdated();
    String msgUsersUpdated();
    String msgErrorUpdatingUser();
    String msgNoChanges();
    String msgErrorGettingUsers();
    String msgErrorConnectingToDB();
    String msgErrorConnection();
    String msgErrorEmptyName();
    String msgErrorNoLibrarySelected();

    String langUploaded();
    String langAddLanguage();
    String langName();
    String langLocaleString();
    String langTranslationFile();
    String langMsgNoFile();

    String userDlgAddNewLanguage();
}
