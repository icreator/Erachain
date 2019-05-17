package org.erachain.gui.items.assets;

import org.erachain.controller.Controller;
import org.erachain.core.account.Account;
import org.erachain.core.account.PrivateKeyAccount;
import org.erachain.core.item.assets.AssetCls;
import org.erachain.core.item.assets.Order;
import org.erachain.core.transaction.CreateOrderTransaction;
import org.erachain.core.transaction.Transaction;
import org.erachain.gui.AccountRenderer;
import org.erachain.gui.MainFrame;
import org.erachain.gui.PasswordPane;
import org.erachain.gui.library.IssueConfirmDialog;
import org.erachain.gui.library.MDecimalFormatedTextField;
import org.erachain.gui.models.AccountsComboBoxModel;
import org.erachain.gui.transaction.CreateOrderDetailsFrame;
import org.erachain.gui.transaction.OnDealClick;
import org.erachain.lang.Lang;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("serial")
public class CreateOrderPanel extends JPanel {
    static Logger LOGGER = LoggerFactory.getLogger(CreateOrderPanel.class.getName());
    public JComboBox<Account> cbxAccount;
    public MDecimalFormatedTextField txtAmountHave;
    public MDecimalFormatedTextField txtPrice;
    private AssetCls have;
    private AssetCls want;
    private JButton sellButton;
    private JComboBox<String> txtFeePow;
    private JTextField txtBuyingAmount;
    private JTextPane superHintText;
    private boolean SHOW_HINTS = false;
    boolean needUpdatePrice = false;
    boolean noUpdatePrice = false;
    
    public CreateOrderPanel(AssetCls have, AssetCls want, boolean buying, String account) {
        this.setLayout(new GridBagLayout());
        this.have = have;
        this.want = want;

        // PADDING
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // LABEL GBC
        GridBagConstraints superhintGBC = new GridBagConstraints();
        superhintGBC.insets = new Insets(0, 5, 5, 0);
        superhintGBC.fill = GridBagConstraints.BOTH;
        superhintGBC.anchor = GridBagConstraints.SOUTHWEST;
        superhintGBC.gridx = 0;
        superhintGBC.gridwidth = 3;
        superhintGBC.weightx = superhintGBC.weighty = 1.0;
        superhintGBC.weighty = 1.0;

        // LABEL GBC
        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.insets = new Insets(0, 5, 5, 0);
        labelGBC.fill = GridBagConstraints.HORIZONTAL;
        labelGBC.anchor = GridBagConstraints.NORTHWEST;
        labelGBC.gridx = 0;

        // DETAIL GBC
        GridBagConstraints detailGBC = new GridBagConstraints();
        detailGBC.insets = new Insets(0, 5, 5, 0);
        detailGBC.fill = GridBagConstraints.HORIZONTAL;
        detailGBC.anchor = GridBagConstraints.NORTHWEST;
        detailGBC.gridx = 1;

        detailGBC.fill = java.awt.GridBagConstraints.HORIZONTAL;
        detailGBC.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        detailGBC.weightx = 1.0;

        // DETAIL GBC
        GridBagConstraints assetHintGBC = new GridBagConstraints();
        assetHintGBC.insets = new Insets(0, 5, 5, 0);
        assetHintGBC.fill = GridBagConstraints.HORIZONTAL;
        assetHintGBC.anchor = GridBagConstraints.FIRST_LINE_START;
        assetHintGBC.fill = java.awt.GridBagConstraints.HORIZONTAL;
        assetHintGBC.weightx = 1.0;

        assetHintGBC.gridx = 2;

        labelGBC.gridy = 0;
        detailGBC.gridy = 0;

        // label buy
        // DETAIL GBC
        GridBagConstraints label_sell_buy = new GridBagConstraints();
        label_sell_buy.insets = new Insets(0, 5, 5, 0);
        label_sell_buy.fill = GridBagConstraints.HORIZONTAL;
        label_sell_buy.anchor = GridBagConstraints.NORTHWEST;
        label_sell_buy.gridx = 0;
        label_sell_buy.gridwidth = 3;

        label_sell_buy.fill = java.awt.GridBagConstraints.HORIZONTAL;
        label_sell_buy.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        label_sell_buy.weightx = 1.0;

        label_sell_buy.gridy = ++labelGBC.gridy;
        detailGBC.gridy = ++detailGBC.gridy;
        JLabel lblWish = new JLabel(
                "<html>" + (buying ? Lang.getInstance().translate("To buy") + ": " + "<b>" + this.want.toString()
                        : Lang.getInstance().translate("To sell") + ": " + "<b>" + this.have.toString()) + "</b></html>");

        this.add(lblWish, label_sell_buy);

        JLabel lblResult = new JLabel(
                "<html>" + (buying ? Lang.getInstance().translate("For") + ": <b>" + this.have.toString()
                        : Lang.getInstance().translate("For") + ": <b>" + this.want.toString()) + "</b></html>");

        // Label sell
        label_sell_buy.gridy = ++labelGBC.gridy;
        detailGBC.gridy = ++detailGBC.gridy;
        this.add(lblResult, label_sell_buy);

        // LABEL FROM
        labelGBC.gridy = ++labelGBC.gridy;
        // JLabel fromLabel = new JLabel(Lang.getInstance().translate("Account")
        // + ":");
        // this.add(fromLabel, labelGBC);

        // COMBOBOX FROM
        detailGBC.gridx = 0;
        detailGBC.gridy = ++detailGBC.gridy;
        detailGBC.gridwidth = 3;
        this.cbxAccount = new JComboBox<Account>(new AccountsComboBoxModel());
        this.cbxAccount.setRenderer(new AccountRenderer(this.have.getKey()));
        // select accounts in combobox
        if (account != "" && account != null) {
            for (int i = 0; this.cbxAccount.getModel().getSize() > i; i++) {
                Account elem = this.cbxAccount.getModel().getElementAt(i);
                if (elem.getAddress().toString().contentEquals(account))
                    this.cbxAccount.setSelectedIndex(i);

                this.cbxAccount.repaint();
            }
            this.cbxAccount.setEnabled(false);
        }
        cbxAccount
                .setPreferredSize(new Dimension(100, cbxAccount.getFontMetrics(cbxAccount.getFont()).getHeight() + 8));

        this.add(this.cbxAccount, detailGBC);
        detailGBC.gridx = 1;
        detailGBC.gridwidth = 1;
        // ASSET HINT
        assetHintGBC.gridy = detailGBC.gridy;
        if (SHOW_HINTS) {
            JLabel accountHintLabel = new JLabel(have.getShortName());
            this.add(accountHintLabel, assetHintGBC);
        }

        // LABEL AMOUNT
        labelGBC.gridy++;

        String mes = buying ? Lang.getInstance().translate("Quantity to buy")
                : Lang.getInstance().translate("Quantity to sell");

        JLabel amountLabel = new JLabel(mes + ":");
        this.add(amountLabel, labelGBC);

        // AMOUNT
        detailGBC.gridy++;
        this.txtAmountHave = new MDecimalFormatedTextField();
        // set scale
        this.txtAmountHave.setScale(have==null? 8: buying? want.getScale() : have.getScale());
        this.add(this.txtAmountHave, detailGBC);

        // ASSET HINT
        assetHintGBC.gridy = detailGBC.gridy;
        if (SHOW_HINTS) {
            JLabel amountHintLabel = new JLabel(buying ? want.getShortName() : have.getShortName());
            this.add(amountHintLabel, assetHintGBC);
        }

        // LABEL PRICE
        labelGBC.gridy++;
        JLabel priceLabel = new JLabel(
                "<html><b>" + Lang.getInstance().translate("Price per unit") + " " + ":</b></html>");
        this.add(priceLabel, labelGBC);
        // PRICE
        detailGBC.gridy++;
        txtPrice = new MDecimalFormatedTextField();
        // set scale

        txtPrice.setScale(setScale(have, want));
        this.add(txtPrice, detailGBC);
        // ASSET HINT
        assetHintGBC.gridy = detailGBC.gridy;
        if (SHOW_HINTS) {
            JLabel priceHintLabel = new JLabel(buying ? have.getShortName() : want.getShortName());
            this.add(priceHintLabel, assetHintGBC);
        }

        if (false & buying) {
            /*
             * //LABEL BUYING PRICE labelGBC.gridy++; JLabel buyingPriceLabel =
             * new JLabel(Lang.getInstance().translate("Buying price") + ":");
             * this.add(buyingPriceLabel, labelGBC);
             *
             * //BUYING PRICE detailGBC.gridy++; txtBuyingPrice = new
             * JTextField(); txtBuyingPrice.setEnabled(false);
             * this.add(txtBuyingPrice, detailGBC);
             *
             * //ASSET HINT assetHintGBC.gridy = detailGBC.gridy; JLabel
             * buyingPriceHintLabel = new JLabel( have.getShort() );
             * this.add(buyingPriceHintLabel, assetHintGBC);
             *
             * //ON PRICE CHANGE txtPrice.getDocument().addDocumentListener(new
             * DocumentListener() { public void changedUpdate(DocumentEvent e) {
             * calculateBuyingPrice(txtBuyingPrice); }
             *
             * public void removeUpdate(DocumentEvent e) {
             * calculateBuyingPrice(txtBuyingPrice); }
             *
             * public void insertUpdate(DocumentEvent e) {
             * calculateBuyingPrice(txtBuyingPrice); } });
             */
        } else {
            // ON PRICE CHANGE
            txtPrice.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (!noUpdatePrice) {
                        needUpdatePrice = false;
                        calculateBuyingAmount(txtBuyingAmount, buying);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (!noUpdatePrice) {
                        needUpdatePrice = false;
                        calculateBuyingAmount(txtBuyingAmount, buying);
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (!noUpdatePrice) {
                        needUpdatePrice = false;
                        calculateBuyingAmount(txtBuyingAmount, buying);
                    }
                }
            });
        }

        // LABEL AMOUNT
        labelGBC.gridy++;

        // mes = buying ? Lang.getInstance().translate("Result") :
        // Lang.getInstance().translate("Total Buy (want)");
        mes = Lang.getInstance().translate("Total");

        JLabel buyingAmountLabel = new JLabel(mes + ":");
        this.add(buyingAmountLabel, labelGBC);

        // AMOUNT
        detailGBC.gridy++;
        txtBuyingAmount = new JTextField();
        txtBuyingAmount.setEditable(false);
        txtBuyingAmount.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        this.add(txtBuyingAmount, detailGBC);

        // ASSET HINT
        assetHintGBC.gridy = detailGBC.gridy;
        if (SHOW_HINTS) {
            JLabel buyingAmountHintLabel = new JLabel(buying ? have.getShortName() : want.getShortName());
            this.add(buyingAmountHintLabel, assetHintGBC);
        }

        // ON PRICE CHANGE
        txtAmountHave.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                needUpdatePrice = true;
                calculateBuyingAmount(txtBuyingAmount, buying);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                needUpdatePrice = true;
                calculateBuyingAmount(txtBuyingAmount, buying);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                needUpdatePrice = true;
                calculateBuyingAmount(txtBuyingAmount, buying);
            }
        });

        // LABEL FEE
        labelGBC.gridy++;
        JLabel feeLabel = new JLabel(Lang.getInstance().translate("Fee Power") + ":");
        this.add(feeLabel, labelGBC);

        // FEE
        detailGBC.gridy++;
        txtFeePow = new JComboBox<String>();
        txtFeePow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8" }));
        txtFeePow.setSelectedIndex(0);
        this.add(txtFeePow, detailGBC);

        // ASSET HINT
        assetHintGBC.gridy = detailGBC.gridy;
        /////JLabel feeHintLabel = new JLabel(Controller.getInstance().getAsset(AssetCls.FEE_KEY).getName());
        ////this.add(feeHintLabel, assetHintGBC);

        // ADD SELL BUTTON
        labelGBC.gridy++;
        labelGBC.gridwidth = 3;

        superHintText = new JTextPane();
        superHintText.setEditable(false);
        superHintText.setBackground(this.getBackground());
        superHintText.setContentType("text/html");

        superHintText.setFont(txtBuyingAmount.getFont());
        superHintText.setText("<html><body style='font-size: 100%'>&nbsp;<br>&nbsp;<br></body></html>");

        // superHintText.setPreferredSize(new Dimension(125, 40));

        JPanel scrollPaneSuperHintText = new JPanel(new BorderLayout());

        scrollPaneSuperHintText.add(superHintText, BorderLayout.SOUTH);

        this.add(scrollPaneSuperHintText, superhintGBC);

        labelGBC.gridy++;

        if (buying) {
            this.sellButton = new JButton(Lang.getInstance().translate("Buy"));
            this.sellButton.setBackground(new Color(204, 255, 204));
        } else {
            this.sellButton = new JButton(Lang.getInstance().translate("Sell"));
            this.sellButton.setBackground(new Color(255, 153, 153));
        }

        sellButton.setEnabled(false);
        // this.sellButton.setPreferredSize(new Dimension(125, 25));
        this.sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSellClick(buying);
            }
        });
        this.add(this.sellButton, labelGBC);
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            new BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void calculateHint() {
        if (!isDigit(this.txtPrice.getText()))
            superHintText.setText("<html><body style='font-size: 100%'>&nbsp;<br>"
                    + Lang.getInstance().translate("Enter correct price.") + "</body></html>");
        else if (!isDigit(this.txtAmountHave.getText()))
            superHintText.setText("<html><body style='font-size: 100%'>&nbsp;<br>"
                    + Lang.getInstance().translate("Enter correct amount.") + "</body></html>");
        else
            superHintText.setText("<html><body style='font-size: 100%'>" + Lang.getInstance()
                    .translate("Give <b>%amount% %have%</b>"
                            + " at the price of <b>%price%&nbsp;%want%</b> per <b>1% %have%</b> that would get "
                            + "<b>%buyingamount%&nbsp;%want%</b>.")
                    .replace("%amount%", this.txtAmountHave.getText()).replace("%have%", have.getShort())
                    .replace("%price%", this.txtPrice.getText()).replace("%want%", want.getShort())
                    .replace("%buyingamount%", this.txtBuyingAmount.getText()) + "</body></html>");
    }

    /*
     * public void calculateBuyingPrice(JTextField target, boolean buying) { try
     * { BigDecimal price = new BigDecimal(txtPrice.getText());
     * target.setText(BigDecimal.ONE.
     * divide(price, RoundingMode.DOWN).toPlainString()); } catch(Exception e) {
     * target.setText("0"); }
     *
     * calculateBuyingAmount(txtBuyingAmount, buying); }
     */

    public void calculateBuyingAmount(JTextField target, boolean buying) {

        int i = 1;
        try {

            BigDecimal amount = new BigDecimal(txtAmountHave.getText());
            i++;
            BigDecimal price = new BigDecimal(txtPrice.getText());

            BigDecimal result;

            if (buying) {
                result = price.multiply(amount).setScale(have.getScale(), RoundingMode.HALF_DOWN);
                if (needUpdatePrice && txtAmountHave.getText().length() > 0) {
                    noUpdatePrice = true;
                    txtPrice.setText(Order.calcPrice(amount, result, 2).toPlainString());
                    noUpdatePrice = false;
                }

            } else {
                result = price.multiply(amount).setScale(want.getScale(), RoundingMode.HALF_DOWN);
                if (needUpdatePrice && txtAmountHave.getText().length() > 0) {
                    noUpdatePrice = true;
                    txtPrice.setText(Order.calcPrice(amount, result, 2).toPlainString());
                    noUpdatePrice = false;
                }
            }
            target.setText(result.toPlainString());

            BigDecimal r = new BigDecimal(target.getText());
            if (r.signum() != 0)
                sellButton.setEnabled(true);
        } catch (Exception e) {
            if (i == 1)
                target.setText("Error: Field 1 must be number. Format ###.#");
            if (i == 2)
                target.setText("Error: Field 2 must be number. Format ###.#");
            // target.setText("0");
            sellButton.setEnabled(false);
        }

        // calculateHint();
    }

    public void onSellClick(boolean buying) {

        // DISABLE
        this.sellButton.setEnabled(false);

        // CHECK IF NETWORK OK
        if (false && Controller.getInstance().getStatus() != Controller.STATUS_OK) {
            // NETWORK NOT OK
            JOptionPane.showMessageDialog(null,
                    Lang.getInstance().translate(
                            "You are unable to send a transaction while synchronizing or while having no connections!"),
                    Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);

            // ENABLE
            this.sellButton.setEnabled(true);

            return;
        }

        // READ CREATOR
        Account sender = (Account) this.cbxAccount.getSelectedItem();

        int feePow;
        BigDecimal amountHave;
        BigDecimal amountWant;
        long parse = 0;
        try {
            // READ FEE
            feePow = Integer.parseInt((String)this.txtFeePow.getSelectedItem());

            // READ AMOUNT
            parse = 1;
            amountHave = new BigDecimal(this.txtAmountHave.getText());

            // READ PRICE
            parse = 2;
            // price = new BigDecimal(this.txtPrice.getText());
            amountWant = new BigDecimal(this.txtBuyingAmount.getText());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            if (parse == 0) {
                JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Invalid fee") + "!",
                        Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
            }
            if (parse == 1) {
                JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Invalid amount") + "!",
                        Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
            }
            if (parse == 2) {
                JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Invalid price") + "!",
                        Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
            }
            this.sellButton.setEnabled(true);
            return;
        }

        if (amountWant.compareTo(new BigDecimal(0)) == 0 || amountHave.compareTo(new BigDecimal(0)) == 0) {

            // DISABLE
            this.sellButton.setEnabled(true);
            return;
        }

        // CREATE ORDER

        // BigDecimal amountWant = amountHave.multiply(price);
        if (buying) {
            BigDecimal amountTemp = amountWant;
            amountWant = amountHave;
            amountHave = amountTemp;
        }

        if (false) {
            // for develop
            JOptionPane.showMessageDialog(new JFrame(), amountHave.toPlainString() + " - " + amountWant.toPlainString(),
                    Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
            this.sellButton.setEnabled(true);
            return;
        }

        // CHECK IF WALLET UNLOCKED
        if (!Controller.getInstance().isWalletUnlocked()) {
            // ASK FOR PASSWORD
            String password = PasswordPane.showUnlockWalletDialog(this);
            if (!Controller.getInstance().unlockWallet(password)) {
                // WRONG PASSWORD
                JOptionPane.showMessageDialog(null, Lang.getInstance().translate("Invalid password"),
                        Lang.getInstance().translate("Unlock Wallet"), JOptionPane.ERROR_MESSAGE);

                // ENABLE
                this.sellButton.setEnabled(true);

                return;
            }
        }

        PrivateKeyAccount creator = Controller.getInstance().getPrivateKeyAccountByAddress(sender.getAddress());
        if (creator == null) {
            JOptionPane.showMessageDialog(new JFrame(),
                    Lang.getInstance().translate(OnDealClick.resultMess(Transaction.PRIVATE_KEY_NOT_FOUND)),
                    Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        Transaction transaction = Controller.getInstance().createOrder(creator, this.have, this.want,
                amountHave.setScale(this.have.getScale(), RoundingMode.HALF_DOWN),
                amountWant.setScale(this.want.getScale(), RoundingMode.HALF_DOWN), feePow);

        String Status_text = "";
        IssueConfirmDialog dd = new IssueConfirmDialog(MainFrame.getInstance(), true, transaction,
                Lang.getInstance().translate("Send Order"), (int) (MainFrame.getInstance().getWidth() / 1.2),
                (int) (MainFrame.getInstance().getHeight() / 1.2), Status_text,
                Lang.getInstance().translate("Confirmation Transaction") + ": "
                        + Lang.getInstance().translate("order creation"));

        CreateOrderDetailsFrame ww = new CreateOrderDetailsFrame((CreateOrderTransaction) transaction);
        dd.jScrollPane1.setViewportView(ww);
        dd.setLocationRelativeTo(null);
        dd.setVisible(true);

        // JOptionPane.OK_OPTION
        if (dd.isConfirm) {

            Integer result = Controller.getInstance().getTransactionCreator().afterCreate(transaction, Transaction.FOR_NETWORK);

            // CHECK VALIDATE MESSAGE
            if (result == Transaction.VALIDATE_OK) {

                JOptionPane.showMessageDialog(new JFrame(), Lang.getInstance().translate("Order has been sent") + "!",
                        Lang.getInstance().translate("Success"), JOptionPane.INFORMATION_MESSAGE);

                // this.txtFeePow.setText("0");
                this.txtAmountHave.setText("0");
                this.txtPrice.setText("0");

            } else {
                JOptionPane.showMessageDialog(new JFrame(),
                        Lang.getInstance().translate(OnDealClick.resultMess(result)),
                        Lang.getInstance().translate("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }
        // ENABLE
        this.sellButton.setEnabled(true);
    }
    // confirm asset & return scale
    private int setScale(AssetCls assetHave, AssetCls assetWant){
        if (assetHave != null && assetWant != null) {
            return assetHave.getScale() + assetWant.getScale() + 3;
        }
        return 8;
    }

}
