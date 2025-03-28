package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * 
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");

        // ログイン
        inputLogin();

        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        // タスク一覧表示
                        taskLogic.showAll(loginUser);
                        // サブメニューへ移動
                        selectSubMenu();
                        break;
                    case "2":
                        // タスク新規登録
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        boolean flg = true;
        while (flg) {
            try {
                // メールアドレスとパスワードを入力してもらう
                // メールアドレス
                System.out.print("メールアドレスを入力してください：");
                String email = reader.readLine();
                // パスワード
                System.out.print("パスワードを入力してください：");
                String password = reader.readLine();

                // 一致しているか確認
                loginUser = userLogic.login(email, password);

                System.out.println("ユーザー名：" + loginUser.getName() + "でログインしました。");
                flg = false;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }

    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        // タスクコード、タスク名、タスクを担当するユーザーコードを入力
        boolean flg = true;

        while (flg) {
            try {
                System.out.print("タスクコードを入力してください：");
                String taskCode = reader.readLine();

                if (!(isNumeric(taskCode))) {
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                System.out.print("タスク名を入力してください：");
                String taskName = reader.readLine();

                if (!(taskName.length() <= 10)) {
                    System.out.println("タスク名は10文字以内で入力してください");
                    System.out.println();
                    continue;
                }

                System.out.print("担当するユーザーのコードを選択してください：");
                String taskUserCode = reader.readLine();

                if (!(isNumeric(taskUserCode))) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                taskLogic.save(Integer.parseInt(taskCode), taskName, Integer.parseInt(taskUserCode), loginUser);
                System.out.println(taskName + "の登録が完了しました。");
                flg = false;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }

    }

    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        boolean flg = true;

        try {
            while (flg) {
                // 1,2どちらか選んでもらう
                System.out.println("以下1~2から好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();
                System.out.println();

                switch (selectMenu) {
                    case "1":
                        // ステータスの更新
                        inputChangeInformation();
                        break;

                    case "2":
                        // メインメニューに戻る
                        System.out.println("メインメニューに戻ります。");
                        flg = false;
                        break;

                    default:
                        System.out.println("選択肢が間違えています。1,2から選んでください。");
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {
        boolean flg = true;
        while (flg) {
            try {
                // 変更を行うタスクコードを入力
                System.out.print("ステータスを変更するタスクコードを入力してください：");
                String taskCode = reader.readLine();

                // タスクコードが数字か確認
                if(!(isNumeric(taskCode))){
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
        
                // どのステータスに変えるか入力
                System.out.println("どのステータスに変更するか選択してください。");
                System.out.println("1. 着手中, 2. 完了");
                System.out.print("選択肢：");
                String selectStatus = reader.readLine();

                // ステータスは数字か確認
                if(!isNumeric(selectStatus)){
                    System.out.println("ステータスは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
                // ステータスは1か2かどちらかなのか確認
                if(!(selectStatus.equals("1") || selectStatus.equals("2"))){
                    System.out.println("ステータスは1・2の中から選択してください");
                    System.out.println();
                    continue;
                }
                taskLogic.changeStatus(Integer.parseInt(taskCode), Integer.parseInt(selectStatus), loginUser);
                System.out.println("ステータスの変更が完了しました。");
                flg = false;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e){
                System.out.println(e.getMessage());
            }
            System.out.println();
        }

    }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    // public void inputDeleteInformation() {
    // }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        return inputText.chars().allMatch(c -> Character.isDigit((char) c));
    }
}