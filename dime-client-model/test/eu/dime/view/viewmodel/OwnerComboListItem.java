/*
* Copyright 2013 by the digital.me project (http:\\www.dime-project.eu).
*
* Licensed under the EUPL, Version 1.1 only (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.dime.view.viewmodel;

import eu.dime.model.displayable.PersonItem;

/**
 *
 * @author simon
 */
public class OwnerComboListItem {
    public PersonItem personItem;
    public String guid;

    public OwnerComboListItem(PersonItem personItem, String guid) {
        this.personItem = personItem;
        this.guid = guid;
    }

    @Override
    public String toString() {
        if (personItem == null) {
            return guid;
        }
        return personItem.getGuid() + " (" + personItem.getName() + ")";
    }
    
}
