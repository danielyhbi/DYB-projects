' ## Module 2/2 ##
' This page shows codes and subroutine that is more Macro-heavy

Public Sub ClearContents()
'
' ClearContents Macro
'
    Worksheets("PY Spring").Cells.Clear
    
    Call ClearDepthTable
    
    Call ClearSAPOutput
    
    UpdateSystemStatus "Re-run to show results"
    
End Sub

Public Sub ClearDepthTable()

    Worksheets("Main Page").Range("A29:C90").ClearContents

End Sub

Public Sub ClearSAPOutput()

    Worksheets("SAP Output").Range("A4:M9999").Clear

End Sub

Public Sub ErrorMessageNoFunction()

    MsgBox "This feature is yet to be developed. :("

End Sub

Private Sub UpdateSystemStatus(msg As String)

    With Worksheets("Main Page")
    
        .Cells(10, 3) = msg
    
    End With
    
End Sub