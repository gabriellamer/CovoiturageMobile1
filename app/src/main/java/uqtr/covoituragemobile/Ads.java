package uqtr.covoituragemobile;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.Ad;

/**
 * Created by gabriellamer on 14-10-18.
 */
public class Ads extends Activity {
    private Ad ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        if (getIntent().hasExtra("adId")) {
            TextView title = (TextView) findViewById(R.id.tvTitle);
            TextView description = (TextView) findViewById(R.id.tvDescription);
            TextView nbPlace = (TextView) findViewById(R.id.tvNbPlace);
            CheckBox driver = (CheckBox) findViewById(R.id.cbDriver);
            CheckBox airConditionner = (CheckBox) findViewById(R.id.cbAirConditionner);
            CheckBox heater = (CheckBox) findViewById(R.id.cbHeater);

            ad = Search.listAds.get(getIntent().getIntExtra("adId", 0));

            title.setText(ad.getTitle());
            description.setText(ad.getDescription());
            nbPlace.setText("" + ad.getNbPlace());
            driver.setChecked(ad.isDriver());
            airConditionner.setChecked(ad.isAirCond());
            heater.setChecked(ad.isHeater());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                return true;
            case R.id.action_add_contact :
                addContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ads_menu, menu);
        return true;
    }

    private void addContact() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ad.getUser().getName())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, ad.getUser().getLastName())
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, ad.getUser().getPhone())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, ad.getUser().getEmail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, ad.getUser().getAddress().getStreetNb() + " " + ad.getUser().getAddress().getStreetName())
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, ad.getUser().getAddress().getCity())
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, ad.getUser().getAddress().getProvince())
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, ad.getUser().getAddress().getPostalCode())
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, "Canada")
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .build());

        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            if (res!=null && res[0]!=null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), ad.getUser().getName() + " " + ad.getUser().getLastName() + " a été ajouté", Toast.LENGTH_SHORT);
                toast1.show();
            }
            else
            {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Contact non ajouté", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
    }
}