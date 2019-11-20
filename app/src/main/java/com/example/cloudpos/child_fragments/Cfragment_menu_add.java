package com.example.cloudpos.child_fragments;


/*메뉴 관리 Fragment에서 신 메뉴를 새로 추가하는 담당 Fragment*/
//implemented by Yang Insu

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudpos.R;
import com.example.cloudpos.connections.InsertMenuTask;
import com.example.cloudpos.data.MenuItem;
import com.example.cloudpos.data.MenuList;
import com.example.cloudpos.data.Restaurant;
import com.example.cloudpos.fragments.FragmentMenu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class Cfragment_menu_add extends Fragment implements View.OnClickListener {


    String menuNo;
    String menuCode;
    String menuName;
    String menuPrice;
    MenuItem addedMenuItem;

    /*기입 정보 자료*/
    EditText menu_no_ET;
    EditText menu_code_ET;
    EditText menu_name_ET;
    EditText menu_price_ET;

    /*이미지 부분*/
    ImageView menuImage;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int CROP_FROM_IMAGE = 2;

    Bitmap photo; //사진이 여기에 저장됨
    private String absolutePath;


    public interface AddUpdater {
        void update(MenuItem menuItem);
    }

    private AddUpdater addUpdater;

    public static Cfragment_menu_add newInstance() {
        return new Cfragment_menu_add();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.menu_add_cfragment, container, false);

        /*XML View 모듈들과 연결 시작*/
        TextView menu_no_TV = (TextView) fv.findViewById(R.id.amenu_no_TV);
        TextView menu_code_TV = (TextView) fv.findViewById(R.id.amenu_code_TV);
        TextView menu_name_TV = (TextView) fv.findViewById(R.id.amenu_name_TV);
        TextView menu_price_TV = (TextView) fv.findViewById(R.id.amenu_price_TV);

        //기입할 정보 연결
        menu_no_ET = (EditText) fv.findViewById(R.id.amenu_no_entry);
        menu_code_ET = (EditText) fv.findViewById(R.id.amenu_code_entry);
        menu_name_ET = (EditText) fv.findViewById(R.id.amenu_name_entry);
        menuName = menu_name_ET.getText().toString();
        menu_price_ET = (EditText) fv.findViewById(R.id.amenu_price_entry);
        menuPrice = menu_price_ET.getText().toString();

        //이미지뷰
        menuImage = fv.findViewById(R.id.amenuImage);

        //Button
        Button menu_add_Btn = (Button) fv.findViewById(R.id.menu_add_cbtn); //메뉴를 등록 버튼
        menu_add_Btn.setOnClickListener(this);
        Button pic_add_Btn = (Button) fv.findViewById(R.id.pUploadBtn); //사진 업로드 버튼
        pic_add_Btn.setOnClickListener(this);
        /*XML View 모듈들과 연결 끝*/


        return fv;
    }

    //추가 버튼 후 등록 모듈
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.pUploadBtn) { //사진 업로드 팝업 해주는 부분

            // 사진을 직접 찍어 올리거나
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    takePhoto();
                }
            };

            // 갤러리에서 고르거나
            DialogInterface.OnClickListener galleryListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pickGallery();
                }
            };

            //취소 하거나
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            AlertDialog show = new AlertDialog.Builder(v.getContext()).setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", galleryListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }


        if (v.getId() == R.id.menu_add_cbtn) {
            addMenu();
//            addUpdater.update(addedMenuItem); //실제로 Singleton MenuList에 넣어주는거는 update 함수가 하니까 FragmentMenu
            // TODO: 여기에 addedMenuItem 을 서버에 등록해주면 된다 => addedMenuItem

        }

    }

    public void addMenu() {
        String newMenuName = menu_name_ET.getText().toString();
        String newMenuPrice = menu_price_ET.getText().toString();
        int newMenuNo = MenuList.getInstance().menuItemArrayList.size() + 1;

        MenuItem newMenuItem = new MenuItem(String.valueOf(newMenuNo), String.valueOf(newMenuNo), newMenuName, newMenuPrice, photo);
        addUpdater.update(newMenuItem);

        InsertMenuTask insertMenuTask = new InsertMenuTask(newMenuItem, absolutePath);
        insertMenuTask.insertMenu();
    }


    //카메라로 찍기
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp_" + String.valueOf(System.currentTimeMillis() + "jpg"); //임시 저장 부분
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    //앨범에서 가져오기
    public void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    //사진 저장

    public void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MenuPhotos";
        File directory_MenuPhotos = new File(dirPath);

        if (!directory_MenuPhotos.exists()) directory_MenuPhotos.mkdir(); //없으면 생성해라

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            //Crop된 사진을 앨범에서 보이게

            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_GALLERY:
                mImageCaptureUri = data.getData();
            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if (resultCode != RESULT_OK) return;
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MenuPhotos/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    photo = extras.getParcelable("data");
                    menuImage.setImageBitmap(photo);
                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;

                }
                File file = new File(mImageCaptureUri.getPath());
                if (file.exists()) file.delete();


        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        addUpdater = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof AddUpdater) {
            addUpdater = (AddUpdater) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }

    //


}
